package org.example;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class SceneObject {
    protected String id;
    protected int shaderProgram;
    protected transient int vaoId, vboId;

    protected float[] verticesFloat;
    protected int[] indices;

    protected Vector3f position;
    protected Vector3f rotation;
    protected Vector3f scale;
    protected Matrix4f localMatrix;


    public SceneObject() {
        verticesFloat = new float[]{
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        };

        indices = new int[] {
                0, 1, 2,

        };

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Vertices
        vboId = glGenBuffers();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(verticesFloat.length);
        vertexBuffer.put(verticesFloat).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);


        //Indices
        int vboId = glGenBuffers();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, loadShaderSource("shaders/default/vertex.glsl"));
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader compilation failed: " + glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, loadShaderSource("shaders/default/fragment.glsl"));
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader compilation failed: " + glGetShaderInfoLog(vertexShader));
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        glValidateProgram(shaderProgram);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

    }

    public void render() {
        glUseProgram(shaderProgram);

        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, 9, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    private String loadShaderSource(String path){
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)){
            if (stream == null) {
                throw new Exception("Cannot find file " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }
    }
}
