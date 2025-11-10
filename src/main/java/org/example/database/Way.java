package org.example.database;

public class Way {
    public String id;
    public long[] nodes;
    public String[] tags;

    public Way(String wayID) {
        this.id = wayID;
        nodes = DbAccess.getNodesFromWay(wayID);
        tags = DbAccess.getTagsFromWay(wayID);
    }
}
