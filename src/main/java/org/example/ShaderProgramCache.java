package org.example;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgramCache {
    Map<String, Integer> shaderMap;
    private static String DEFAULT_PATH = "shaders/";

    public ShaderProgramCache() {
        shaderMap = new HashMap<>();
        shaderMap.put("default", createShaderProgram("default"));
    }

    public Map<String, Integer> getShaderMap() {
        return shaderMap;
    }

    public int getShader(String shaderName) {
        if (!shaderMap.containsKey(shaderName)) {
            return shaderMap.get("default");
        }
        return shaderMap.get(shaderName);
    }

    public int createShaderProgram(String name) {
        String vertexPath = DEFAULT_PATH + name + "/vertex.glsl";
        String fragmentPath = DEFAULT_PATH + name + "/fragment.glsl";

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, loadShaderSource(vertexPath));
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader compilation failed: " + glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, loadShaderSource(fragmentPath));
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader compilation failed: " + glGetShaderInfoLog(fragmentShader));
        }

        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Shader program linking failed: " + glGetProgramInfoLog(program));
        }

        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Shader program validation failed: " + glGetProgramInfoLog(program));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        return program;
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
