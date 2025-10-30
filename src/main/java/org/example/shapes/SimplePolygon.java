package org.example.shapes;

public class SimplePolygon {

    public static float[] getVerticesFloat() {
        return new float[] {
                -3f, 2f, 0f,
                -1f, 3f, 0f,
                1f, 2f, 0f,
                2f, 3f, 0f,
                1f, -1f, 0f,
                -1f, 2.5f, 0f,
                -2f, 0f, 0f,
        };
    }

    public static int[] getIndices() {
        return new int[] {
                0, 1, 2,
                2, 3, 4,
        };
    }
}
