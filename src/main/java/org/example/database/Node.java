
package org.example.database;

import org.joml.Vector3f;

public class Node {

    public static final int COORD_MULTIPLIER = 10000;

    public String id;
    public double lat;
    public double lon;

    public Vector3f coords = new Vector3f();

    public Node(String nodeID) {
        id = nodeID;

        double[] latLon = DbAccess.getLatLonFromNode(id);

        lat = latLon[0];
        lon = latLon[1];

        updateCoords();
    }

    public Node(String nodeID, double latitude, double longitude) {
        id = nodeID;
        lat = latitude;
        lon = longitude;

        updateCoords();
    }

    private void updateCoords() {
        // Coordinates are swapped lat/lon for converting to X/Y dimensions.
        coords.x = (float) (lon * COORD_MULTIPLIER);
        coords.y = (float) (lat * COORD_MULTIPLIER);
    }

    @Override
    public String toString() {
        return id + " - " + lat + ", " + lon;
    }
}
