package org.example;

import org.example.shapes.SimplePolygon;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.bartlouwers.earcut4j.Earcut;

public class EarClipping {

    public static int[] getTrianglesFromVertices(List<Vector3f> vertices, Map<Vector3f, Integer> map) {

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

            if (isEar(vertices, vertices.get(i), vertices.get(previousVertex), vertices.get(nextVertex))) {
                indices[indicesIndex++] = map.get(vertices.get(previousVertex));
                indices[indicesIndex++] = map.get(vertices.get(i));
                indices[indicesIndex++] = map.get(vertices.get(nextVertex));
                vertices.remove(i);
                i = 0;
            }
        }

        return indices;
    }

    private static boolean isEar(List<Vector3f> vertices, Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {

        Vector3f testPoint = new Vector3f(-1f, 2.5f, 0.0f);
        if (isConvex(originVertex, previousVertex, nextVertex)) {
            if (!pointInTriangle(vertices, originVertex, previousVertex, nextVertex)) {
                return true;
            }
            else {
                System.out.println(testPoint + " is inside triangle " + previousVertex + originVertex + nextVertex);
                return false;
            }
        }
        else {
            return false;
        }
    }

    private static boolean isConvex(Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {
        Vector3f a = new Vector3f();
        previousVertex.sub(originVertex, a);
        Vector3f b = new Vector3f();
        nextVertex.sub(originVertex, b);

        float dotProduct = a.x * b.y - a.y * b.x;

        return dotProduct > 0;
    }

    private static float sign(Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {
        return (previousVertex.x - nextVertex.x) * (originVertex.y - nextVertex.y) - (originVertex.x - nextVertex.x) * (previousVertex.y - nextVertex.y);

    }
    private static boolean pointInTriangle(List<Vector3f> points, Vector3f originVertex, Vector3f previousVertex, Vector3f nextVertex) {
        Vector3f ab = new Vector3f();
        Vector3f bc = new Vector3f();
        Vector3f ca = new Vector3f();

        Vector3f ap = new Vector3f();
        Vector3f bp = new Vector3f();
        Vector3f cp = new Vector3f();

        for (Vector3f point : points) {
            originVertex.sub(previousVertex, ab);
            nextVertex.sub(originVertex, bc);
            previousVertex.sub(nextVertex, ca);

            point.sub(previousVertex, ap);
            point.sub(originVertex, bp);
            point.sub(nextVertex, cp);

            Vector3f cross1 = new Vector3f();
            Vector3f cross2 = new Vector3f();
            Vector3f cross3 = new Vector3f();

            ab.cross(ap, cross1);
            bc.cross(bp, cross2);
            ca.cross(cp, cross3);

            if (cross1.x > 0f || cross2.x > 0f || cross3.x > 0f) {
                return true;
            }
        }
        return false;
    }
}
