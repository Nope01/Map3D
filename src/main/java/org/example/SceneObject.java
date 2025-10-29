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

        ObjectUtils.bindVerticesList(verticesFloat);
        ObjectUtils.bindIndicesList(indices);
    }

    public void render() {
        glUseProgram(shaderProgram);

        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, 9, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void setShader(int shaderProgram) {
        this.shaderProgram = shaderProgram;
    }
}
