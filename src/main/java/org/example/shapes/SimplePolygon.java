package org.example.shapes;

import org.joml.Vector3f;

import java.util.Objects;

public class SimplePolygon {
    static float[] vertices = new float[] {
            -3f, 2f, 0f,
            -1f, 3f, 0f,
            1f, 2f, 0f,
            2f, 3f, 0f,
            1f, -1f, 0f,
            0f, 1f, 0f,
            -2f, 0f, 0f,
    };

    public static float[] getVerticesFloat() {
        return vertices;
    }

    public static int[] getIndices() {
        return new int[] {
                0, 1, 2,
                2, 3, 4,
        };
    }

    public static int vertexToIndex(Vector3f vertex) {
        if (Objects.equals(vertex, new Vector3f(-3f, 2f, 0f))) {
            return 0;
        }
        if (Objects.equals(vertex, new Vector3f(-1f, 3f, 0f))) {
            return 1;
        }
        if (Objects.equals(vertex, new Vector3f(1f, 2f, 0f))) {
            return 2;
        }
        if (Objects.equals(vertex, new Vector3f(2f, 3f, 0f))) {
            return 3;
        }
        if (Objects.equals(vertex, new Vector3f(1f, -1f, 0f))) {
            return 4;
        }
        if (Objects.equals(vertex, new Vector3f(0f, 1f, 0f))) {
            return 5;
        }
        if (Objects.equals(vertex, new Vector3f(-2f, 0f, 0f))) {
            return 6;
        }
        return -1;
    }

}
