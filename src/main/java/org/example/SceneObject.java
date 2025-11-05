package org.example;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
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
    protected Matrix4f worldMatrix;


    public SceneObject() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);

        localMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();

    }

    public void update() {
        localMatrix.identity()
                .translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
        worldMatrix.set(localMatrix);
    }

    public void render() {
        glUseProgram(shaderProgram);

        int modelLoc = glGetUniformLocation(shaderProgram, "model");
        glUniformMatrix4fv(modelLoc, false, worldMatrix.get(new float[16]));
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void setShader(int shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void bindGeometry() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        ObjectUtils.bindVerticesList(verticesFloat);
        ObjectUtils.bindIndicesList(indices);
    }
}
