package org.example.database;

public class Way {


    public long id;
    public long[] nodes;
    public String[] tags;

    public Way(long wayID) {
        this.id = wayID;
        nodes = DbAccess.getNodesFromWay(wayID);
        tags = DbAccess.getTagsFromWay(wayID);

        for (long node : nodes) {
            System.out.println(node);
        }

        for (String tag : tags) {
            System.out.println(tag);
        }
    }
}
