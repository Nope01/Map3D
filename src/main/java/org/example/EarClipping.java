package org.example;

import org.joml.Vector3f;

import java.util.List;

public class EarClipping {

    public static int[] getTrianglesFromVertices(List<Vector3f> vertices) {

        int[] indices = new int[(vertices.size()-2)*3];
        int indicesIndex = 0;

        int previousVertex;
        int nextVertex;

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.size() <= 3) {
                return indices;
            }
            if (i == 0) {
                previousVertex = vertices.size()-1;
            }
            else {
                previousVertex = i-1;
            }

            if (i == vertices.size()-1) {
                nextVertex = 0;
            }
            else {
                nextVertex = i+1;
            }

            if (isEar(vertices.get(i), vertices.get(previousVertex), vertices.get(nextVertex))) {
                indices[indicesIndex++] = previousVertex;
                indices[indicesIndex++] = i;
                indices[indicesIndex++] = nextVertex;
                vertices.remove(i);
                i = 0;
            }
        }

        return indices;
    }

    private static boolean isEar(Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {

        if (isConvex(originVertex, previousVertex, nextVertex) && !containsOtherVertex()) {
            return true;
        }
        else {
            return false;
        }
    }

    private static boolean isConvex(Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {
        Vector3f a = new Vector3f();
        previousVertex.sub(originVertex, a);
        Vector3f b =  new Vector3f();
        nextVertex.sub(originVertex, b);

        float dotProduct = a.x * b.y - a.y * b.x;

        return dotProduct > 0;
    }

    private static boolean containsOtherVertex() {
        return false;
    }
}
