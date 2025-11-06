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
}
