package org.example.database;

public class Way {
    public String id;
    public String[] nodeIDs;
    public String[] tags;

    public Way(String wayID) {
        this.id = wayID;
        nodeIDs = DbAccess.getNodesFromWay(wayID);
        tags = DbAccess.getTagsFromWay(wayID);
    }
}
