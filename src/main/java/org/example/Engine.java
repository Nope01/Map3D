package org.example;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;
import static org.lwjgl.opengl.GL43C.GL_DEBUG_OUTPUT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {
    private long window;
    private long oldTime;
    private int width;
    private int height;

    public void startEngine() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        System.out.println("Engine starting");

        // Initialize GLFW
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Monitor size
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = vidmode.width();
        height = vidmode.height();

        //Make a window
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, "DND map maker", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(window, arrWidth, arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];

        // Set up OpenGL
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glViewport(0, 0, width, height);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            if (width > 0 && height > 0) {
                glViewport(0, 0, width, height);
            }
        });

    }

    private void loop() {
        IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuf = BufferUtils.createIntBuffer(1);

        while (!glfwWindowShouldClose(window)) {
            glfwGetFramebufferSize(window, widthBuf, heightBuf);
            int width = widthBuf.get(0);
            int height = heightBuf.get(0);

            if (width <= 0 || height <= 0) {
                // Skip rendering if size is invalid (e.g., minimized)
                glfwPollEvents();
                continue;
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            long time = System.currentTimeMillis();
            float deltaTime = (time - oldTime) / 1000f;
            oldTime = time;

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
