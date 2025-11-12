package org.example.database;

import java.sql.*;

public class Node {
    public String id;
    public double lat;
    public double lon;

    public Node (String nodeID) {
        id = nodeID;
        double[] coords = DbAccess.getLatLonFromNode(id);
        lat = coords[0];
        lon = coords[1];
    }

    public String toString() {
        return (id + " - " + lat + ", " + lon);
    }

}
