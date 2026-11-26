
package org.example.database;

import java.sql.*;
import java.util.*;

public class DbAccess {

    public static String url =
            "jdbc:postgresql://localhost:5432/Australia";

    public static String user = "postgres";
    public static String password = "nope";

    private static final int NODE_BATCH_SIZE = 10000;

    public static Connection connectToDB() {

        try {
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        return null;
    }

    public static List<Node> getNodesFromWay(String id) {

        String sql = "SELECT nodes FROM ways WHERE id = ?";

        try (Connection connection = connectToDB()) {

            if (connection == null) return new ArrayList<>();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setLong(1, Long.parseLong(id));

                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        return sqlStringToNodeList(
                                rs.getString("nodes")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }


    public static List<Tag> getTagsFromWay(String id) {

        String sql = """
        SELECT key, value
        FROM ways,
        LATERAL jsonb_each_text(tags)
        WHERE id = ?
        """;

        List<Tag> tags = new ArrayList<>();

        try (Connection connection = connectToDB()) {

            if (connection == null) return tags;

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setLong(1, Long.parseLong(id));

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        tags.add(new Tag(
                                rs.getString("key"),
                                rs.getString("value")
                        ));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return tags;
    }


    public static double[] getLatLonFromNode(String id) {

        String sql = "SELECT lat, lon FROM nodes WHERE id = ?";

        try (Connection connection = connectToDB()) {

            if (connection == null) {
                return new double[]{-999, -999};
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setLong(1, Long.parseLong(id));

                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        return new double[]{
                                rs.getDouble("lat"),
                                rs.getDouble("lon")
                        };
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return new double[]{-999, -999};
    }




    public static List<Way> getAllWaysAroundAPoint(
            double lat,
            double lon
    ) {
        return getAllWaysAroundAPoint(
                lat,
                lon,
                1000,
                "{\"highway\":\"tertiary\"}"
        );
    }


    public static List<Way> getAllWaysAroundAPoint(
            double lat,
            double lon,
            double radius,
            String tagFilter
    ) {

        List<Way> ways = new ArrayList<>();

        try (Connection connection = connectToDB()) {

            if (connection == null) {
                return ways;
            }

            //Get ways around a point
            ways = getNearbyWays(
                    connection,
                    lat,
                    lon,
                    radius,
                    tagFilter
            );

            if (ways.isEmpty()) {
                return ways;
            }

           //Get node coords from list of node IDs in way
            Map<String, Node> nodes = getNodesForWays(
                    connection,
                    ways
            );

            //Populate each way using its original node order.
            for (Way way : ways) {

                for (String nodeID : way.nodeIDs) {

                    Node node = nodes.get(nodeID);

                    if (node != null) {
                        way.nodes.add(node);
                    }
                }
            }

            return ways;

        } catch (SQLException e) {

            System.err.println(
                    "Error retrieving nearby ways: " + e.getMessage()
            );

            return new ArrayList<>();
        }
    }


    private static List<Way> getNearbyWays(
            Connection connection,
            double lat,
            double lon,
            double radius,
            String tagFilter
    ) throws SQLException {

        String sql = """
            SELECT
                w.id,
                w.nodes,
                t.key AS tag_key,
                t.value AS tag_value

            FROM ways w

            LEFT JOIN LATERAL
                jsonb_each_text(w.tags) AS t(key, value)
                ON TRUE

            WHERE w.geom IS NOT NULL

            AND ST_DWithin(
                w.geom::geography,
                ST_SetSRID(
                    ST_MakePoint(?, ?),
                    4326
                )::geography,
                ?
            )

            AND jsonb_exists(w.tags, ?)

            ORDER BY w.id
            """;

        // LinkedHashMap maintains the order of retrieved ways.
        Map<String, Way> wayMap = new LinkedHashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Longitude first, latitude second.
            stmt.setDouble(1, lon);
            stmt.setDouble(2, lat);

            stmt.setDouble(3, radius);

            // Example: {"highway":"tertiary"}
            stmt.setString(4, tagFilter);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    String wayID = rs.getString("id");

                    Way way = wayMap.get(wayID);

                    // Only construct the Way once.
                    if (way == null) {

                        Array sqlNodes = rs.getArray("nodes");

                        List<String> nodeIDs = new ArrayList<>();

                        try {

                            Object[] nodeArray =
                                    (Object[]) sqlNodes.getArray();

                            for (Object nodeID : nodeArray) {
                                nodeIDs.add(
                                        String.valueOf(nodeID)
                                );
                            }

                        } finally {
                            sqlNodes.free();
                        }

                        way = new Way(
                                wayID,
                                nodeIDs,
                                new ArrayList<>()
                        );

                        wayMap.put(wayID, way);
                    }

                    // Each way can have multiple tags.
                    String tagKey = rs.getString("tag_key");
                    String tagValue = rs.getString("tag_value");

                    if (tagKey != null) {
                        way.tags.add(new Tag(tagKey, tagValue));
                    }
                }
            }
        }

        return new ArrayList<>(wayMap.values());
    }



    private static Map<String, Node> getNodesForWays(
            Connection connection,
            List<Way> ways
    ) throws SQLException {

        Map<String, Node> nodes = new HashMap<>();

        // Collect all unique node IDs.
        Set<String> uniqueNodeIDs = new HashSet<>();

        for (Way way : ways) {
            uniqueNodeIDs.addAll(way.nodeIDs);
        }

        if (uniqueNodeIDs.isEmpty()) {
            return nodes;
        }

        List<String> nodeIDList = new ArrayList<>(uniqueNodeIDs);

        String sql = """
            SELECT
                id,
                lat,
                lon
            FROM nodes
            WHERE id = ANY(?)
            """;

        // Split large requests into batches.
        for (
                int i = 0;
                i < nodeIDList.size();
                i += NODE_BATCH_SIZE
        ) {

            List<String> batch = nodeIDList.subList(
                    i,
                    Math.min(i + NODE_BATCH_SIZE, nodeIDList.size())
            );

            Long[] ids = batch.stream()
                    .map(Long::valueOf)
                    .toArray(Long[]::new);

            Array sqlArray = connection.createArrayOf(
                    "bigint",
                    ids
            );

            try (PreparedStatement stmt =
                         connection.prepareStatement(sql)) {

                stmt.setArray(1, sqlArray);

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        String id = rs.getString("id");

                        double lat = rs.getDouble("lat");
                        double lon = rs.getDouble("lon");

                        // Uses the preloaded-coordinate constructor.
                        Node node = new Node(id, lat, lon);

                        nodes.put(id, node);
                    }
                }

            } finally {
                sqlArray.free();
            }
        }

        return nodes;
    }



    private static List<Node> sqlStringToNodeList(String ids) {

        String[] stringNodes =
                ids.substring(1, ids.length() - 1).split(",");

        List<Node> nodesList = new ArrayList<>();

        for (String nodeID : stringNodes) {
            nodesList.add(new Node(nodeID));
        }

        return nodesList;
    }


    private static List<Tag> sqlStringToTagList(String tags) {

        List<Tag> tagsList = new ArrayList<>();

        if (tags == null || tags.equals("{}")) {
            return tagsList;
        }

        return tagsList;
    }
}
