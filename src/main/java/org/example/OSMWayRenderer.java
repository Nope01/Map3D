
package org.example;

import org.joml.Vector3f;
import org.example.Polygon;

import java.util.ArrayList;
import java.util.List;
import org.example.PolygonTriangulator.*;

import static org.lwjgl.opengl.GL11.*;

public class OSMWayRenderer {

    public enum WayType {
        POLYGON,
        LINE
    }

    public static class WayGeometry {
        public final float[] vertices;
        public final int[] indices;
        public final WayType type;

        public WayGeometry(
                float[] vertices,
                int[] indices,
                WayType type
        ) {
            this.vertices = vertices;
            this.indices = indices;
            this.type = type;
        }
    }

    /**
     * Creates geometry from an OSM way.
     *
     * @param nodes Ordered way nodes.
     * @param closed Whether the original OSM way is closed.
     */
    public static WayGeometry createGeometry(
            List<Vector3f> nodes,
            boolean closed
    ) {

        if (nodes == null || nodes.size() < 2) {
            throw new IllegalArgumentException(
                    "Way requires at least 2 nodes"
            );
        }

        if (closed) {
            return createPolygon(nodes);
        }

        return createLine(nodes);
    }

    private static WayGeometry createPolygon(
            List<Vector3f> nodes
    ) {

        List<Vector3f> ring = new ArrayList<>(nodes);

        // Remove duplicate closing vertex.
        if (ring.size() > 1) {
            Vector3f first = ring.get(0);
            Vector3f last = ring.get(ring.size() - 1);

            if (first.x == last.x && first.y == last.y) {
                ring.remove(ring.size() - 1);
            }
        }

        if (ring.size() < 3) {
            throw new IllegalArgumentException(
                    "Closed way requires at least 3 unique nodes"
            );
        }

        List<List<Vector3f>> rings = List.of(ring);

        PolygonTriangulator.Result result =
                PolygonTriangulator.triangulate(rings);

        return new WayGeometry(
                result.getVerticesFloat(),
                result.indices,
                WayType.POLYGON
        );
    }

    private static WayGeometry createLine(
            List<Vector3f> nodes
    ) {

        float[] vertices = new float[nodes.size() * 3];

        int[] indices = new int[(nodes.size() - 1) * 2];

        for (int i = 0; i < nodes.size(); i++) {
            Vector3f node = nodes.get(i);

            vertices[i * 3] = node.x;
            vertices[i * 3 + 1] = node.y;
            vertices[i * 3 + 2] = node.z;
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            indices[i * 2] = i;
            indices[i * 2 + 1] = i + 1;
        }

        return new WayGeometry(
                vertices,
                indices,
                WayType.LINE
        );
    }
}
