package org.example.database;

import java.util.List;

public class Way {
    public String id;
    public List<Node> nodes;
    public List<Tag> tags;

    public Way(String wayID) {
        this.id = wayID;
        nodes = DbAccess.getNodesFromWay(wayID);
        tags = DbAccess.getTagsFromWay(wayID);
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
