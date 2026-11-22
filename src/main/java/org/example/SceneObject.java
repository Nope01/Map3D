package org.example;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class SceneObject {

    protected String id;
    protected ShaderProgramCache shaderProgramCache;
    protected int shaderProgram;

    protected transient int vaoId, vboId;

    protected float[] verticesFloat;
    protected int[] indices;

    // Determines how this object is rendered.
    protected int drawMode = GL_TRIANGLES;

    protected Vector3f position;
    protected Vector3f rotation;
    protected Vector3f scale;

    protected Matrix4f localMatrix;
    protected Matrix4f worldMatrix;

    public SceneObject(ShaderProgramCache shaderProgramCache) {

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);

        localMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();

        this.shaderProgramCache = shaderProgramCache;

        setShader("default");
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

        glUniformMatrix4fv(
                modelLoc,
                false,
                worldMatrix.get(new float[16])
        );

        // Set OpenGL state based on the geometry type.
        if (drawMode == GL_TRIANGLES) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glFrontFace(GL_CW);
        } else {
            glDisable(GL_CULL_FACE);
        }

        glBindVertexArray(vaoId);

        glDrawElements(
                drawMode,
                indices.length,
                GL_UNSIGNED_INT,
                0
        );

        glBindVertexArray(0);
    }

    public void setShader(String shaderName) {
        this.shaderProgram = shaderProgramCache.getShader(shaderName);
    }

    /**
     * Sets the OpenGL primitive type used to render this object.
     *
     * Examples:
     * GL_TRIANGLES - filled polygons
     * GL_LINES     - individual line segments
     * GL_LINE_STRIP - connected line segments
     */
    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public void bindGeometry() {

        vaoId = glGenVertexArrays();

        glBindVertexArray(vaoId);

        ObjectUtils.bindVerticesList(verticesFloat);
        ObjectUtils.bindIndicesList(indices);

        glBindVertexArray(0);
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        String newLine = System.lineSeparator();

        for (int i = 0; i < verticesFloat.length; i += 3) {

            stringBuilder.append(verticesFloat[i]).append(", ");
            stringBuilder.append(verticesFloat[i + 1]).append(", ");
            stringBuilder.append(verticesFloat[i + 2]);
            stringBuilder.append(newLine);
        }

        return stringBuilder.toString();
    }

    // Position setting

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setPosition(Vector3fc position) {
        this.position.set(position);
    }
}