package org.example.database;

import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.*;

public class Way {
    public String id;
    public List<Node> nodes;
    public List<Tag> tags;

    public Way(String wayID) {
        this.id = wayID;
        nodes = DbAccess.getNodesFromWay(wayID);
        tags = DbAccess.getTagsFromWay(wayID);

        sortNodesClockwise();
    }

    public void sortNodesClockwise() {
        double centerX = 0d;
        double centerY = 0d;

        for (Node node : nodes) {
            centerX += node.coords.x;
            centerY += node.coords.y;
        }
        centerX /= nodes.size();
        centerY /= nodes.size();

        Vector3d centroid = new Vector3d(centerX, centerY, 0);

        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                double angle1 = Math.atan2(node1.coords.y - centroid.y, node1.coords.x - centroid.x);
                double angle2 = Math.atan2(node2.coords.y - centroid.y, node2.coords.x - centroid.x);
                return Double.compare(angle2, angle1);
            }
        });
    }

    public List<Vector3f> wayVectors() {
        List<Vector3f> vertices = new ArrayList<>();
        for (Node node : nodes) {
            vertices.add(node.coords);
        }
        return vertices;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String newLine = System.lineSeparator();
        for (int i = 0; i < nodes.size(); i++) {
            stringBuilder.append("Node ").append(i).append(": ");
            stringBuilder.append(nodes.get(i).toString());
            stringBuilder.append(newLine);
        }
        stringBuilder.append(newLine);
        for (Tag tag : tags) {
            stringBuilder.append(tag.toString());
            stringBuilder.append(newLine);
        }
        return stringBuilder.toString();
    }
}
