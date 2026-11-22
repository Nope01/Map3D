
package org.example;

import org.joml.Vector3f;
import org.maplibre.earcut4j.Earcut;

import java.util.ArrayList;
import java.util.List;

public class PolygonTriangulator {

    private static final double EARTH_RADIUS = 6378137.0;
    private static final float EPSILON = 0.0000001f;

    private PolygonTriangulator() {
    }

    public static class Result {
        public final List<Vector3f> vertices;
        public final int[] indices;

        public Result(List<Vector3f> vertices, int[] indices) {
            this.vertices = vertices;
            this.indices = indices;
        }

        /**
         * Converts vertices into an interleaved XYZ float array.
         */
        public float[] getVerticesFloat() {
            float[] result = new float[vertices.size() * 3];

            for (int i = 0; i < vertices.size(); i++) {
                Vector3f v = vertices.get(i);

                result[i * 3] = v.x;
                result[i * 3 + 1] = v.y;
                result[i * 3 + 2] = v.z;
            }

            return result;
        }
    }

    /**
     * Triangulates one OSM polygon.
     *
     * rings[0] = outer ring
     * rings[1..n] = inner rings (holes)
     *
     * Each ring must contain vertices in boundary order.
     *
     * Coordinates:
     * x = longitude
     * y = latitude
     * z = elevation
     */
    public static Result triangulate(
            List<List<Vector3f>> rings
    ) {

        if (rings == null || rings.isEmpty()) {
            throw new IllegalArgumentException(
                    "Polygon requires an outer ring"
            );
        }

        List<Vector3f> vertices = new ArrayList<>();
        List<Integer> holeIndices = new ArrayList<>();

        // Establish a local projection origin.
        List<Vector3f> outer = cleanRing(rings.get(0));

        if (outer.size() < 3) {
            throw new IllegalArgumentException(
                    "Outer ring requires at least 3 vertices"
            );
        }

        double originLon = outer.stream()
                .mapToDouble(v -> v.x)
                .average()
                .orElseThrow();

        double originLat = outer.stream()
                .mapToDouble(v -> v.y)
                .average()
                .orElseThrow();

        double cosLatitude = Math.cos(
                Math.toRadians(originLat)
        );

        List<List<Vector3f>> cleanedRings = new ArrayList<>();
        cleanedRings.add(outer);

        for (int i = 1; i < rings.size(); i++) {
            List<Vector3f> hole = cleanRing(rings.get(i));

            if (hole.size() >= 3) {
                cleanedRings.add(hole);
            }
        }

        // Flatten rings into one vertex list.
        for (int ringIndex = 0;
             ringIndex < cleanedRings.size();
             ringIndex++) {

            List<Vector3f> ring = cleanedRings.get(ringIndex);

            if (ringIndex > 0) {
                holeIndices.add(vertices.size());
            }

            for (Vector3f vertex : ring) {
                vertices.add(new Vector3f(vertex));
            }
        }

        // Earcut expects flattened 2D coordinates.
        double[] coordinates = new double[vertices.size() * 2];

        for (int i = 0; i < vertices.size(); i++) {

            Vector3f vertex = vertices.get(i);

            double lon = Math.toRadians(vertex.x);
            double lat = Math.toRadians(vertex.y);

            double originLonRad = Math.toRadians(originLon);
            double originLatRad = Math.toRadians(originLat);

            // Local equirectangular projection, in metres.
            double projectedX =
                    EARTH_RADIUS *
                            (lon - originLonRad) *
                            cosLatitude;

            double projectedY =
                    EARTH_RADIUS *
                            (lat - originLatRad);

            coordinates[i * 2] = projectedX;
            coordinates[i * 2 + 1] = projectedY;
        }

        int[] holes = holeIndices.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        int[] indices = Earcut.earcut(
                        coordinates,
                        holes.length == 0 ? null : holes,
                        2
                ).stream()
                .mapToInt(Integer::intValue)
                .toArray();

        // Guarantee counter-clockwise triangle winding.
        ensureCounterClockwise(coordinates, indices);

        return new Result(vertices, indices);
    }

    /**
     * Removes duplicate consecutive vertices and the repeated
     * final vertex commonly used to close OSM rings.
     */
    private static List<Vector3f> cleanRing(
            List<Vector3f> ring
    ) {
        List<Vector3f> result = new ArrayList<>();

        if (ring == null) {
            return result;
        }

        for (Vector3f vertex : ring) {

            if (vertex == null) {
                continue;
            }

            if (result.isEmpty() ||
                    !samePosition(result.get(result.size() - 1), vertex)) {

                result.add(new Vector3f(vertex));
            }
        }

        // OSM rings commonly repeat the first node at the end.
        if (result.size() > 1 &&
                samePosition(result.get(0), result.get(result.size() - 1))) {

            result.remove(result.size() - 1);
        }

        return result;
    }

    private static boolean samePosition(
            Vector3f a,
            Vector3f b
    ) {
        return Math.abs(a.x - b.x) < EPSILON &&
                Math.abs(a.y - b.y) < EPSILON;
    }

    /**
     * Earcut returns triangle indices. Check each triangle's
     * winding and swap the last two indices when necessary.
     */
    private static void ensureCounterClockwise(
            double[] coordinates,
            int[] indices
    ) {

        for (int i = 0; i < indices.length; i += 3) {

            int a = indices[i];
            int b = indices[i + 1];
            int c = indices[i + 2];

            double ax = coordinates[a * 2];
            double ay = coordinates[a * 2 + 1];

            double bx = coordinates[b * 2];
            double by = coordinates[b * 2 + 1];

            double cx = coordinates[c * 2];
            double cy = coordinates[c * 2 + 1];

            double cross =
                    (bx - ax) * (cy - ay) -
                            (by - ay) * (cx - ax);

            if (cross < 0) {
                indices[i + 1] = c;
                indices[i + 2] = b;
            }
        }
    }
}
