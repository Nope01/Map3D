package org.example.database;

import org.joml.Vector3d;
import org.joml.Vector3f;

import java.sql.*;

public class Node {
    public String id;
    public double lat;
    public double lon;
    //Coords are swapped lat and lon for converting to x y dimensions
    public Vector3f coords = new Vector3f();

    public Node (String nodeID) {
        id = nodeID;
        double[] latLon = DbAccess.getLatLonFromNode(id);
        lat = latLon[0];
        lon = latLon[1];
        coords.x = (float) (lon*10000);
        coords.y = (float) (lat*10000);
    }


    public String toString() {
        return (id + " - " + lat + ", " + lon);
    }

}
