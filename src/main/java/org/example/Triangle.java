package org.example;

public class Triangle {

    public static float[] getVerticesFloat() {
        return new float[]{
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        };
    }

    public static int[] getIndices() {
        return new int[] {
                0, 1, 2,
        };
    }
}
