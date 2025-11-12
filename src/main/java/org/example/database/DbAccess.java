package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbAccess {
    public static String url = "jdbc:postgresql://localhost:5432/postgres";
    public static String user = "postgres";
    public static String password = "nope";

    public static Connection connectToDB() {
        try  {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database!");
            return connection;


        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        return null;

    }

    public static List<Node> getNodesFromWay(String id) {
        try (Connection connection = DbAccess.connectToDB()){
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM way WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            System.out.println("Results got!");
            return sqlStringToNodeList(resultSet.getString(2));

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());;
        }
        return null;
    }

    public static List<Tag> getTagsFromWay(String id) {
        try (Connection connection = DbAccess.connectToDB()){
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM way WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            System.out.println("Results got!");
            return sqlStringToTagList(resultSet.getString(3));

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());;
        }
        return null;
    }

    public static double[] getLatLonFromNode(String id) {
        try (Connection connection = DbAccess.connectToDB()) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM node WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return sqlStringToDoubleList(resultSet.getString(2) + "," + resultSet.getString(3));


        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return new double[]{-999};
    }

    private static List<Node> sqlStringToNodeList(String ids) {
        //Split string into node IDs, then make a new node object using id and another sql query on constructor
        String[] stringNodes = ids.substring(1, ids.length()-1).split(",");
        List<Node> nodesList = new ArrayList<>();
        for (int i = 0; i < stringNodes.length; i++) {
            nodesList.add(new Node(stringNodes[i]));
        }
        return nodesList;
    }

    private static List<Tag> sqlStringToTagList(String tags) {
        String[] stringTags = tags.substring(1, tags.length()-1).split(",");
        List<Tag> tagsList = new ArrayList<>();
        for (int i = 0; i < stringTags.length; i+= 2) {
            tagsList.add(new Tag(stringTags[i], stringTags[i+1]));
        }
        return tagsList;
    }


    private static double[] sqlStringToDoubleList(String coords) {
        String[] stringCoords = coords.substring(1, coords.length()-1).split(",");
        double[] doubleCoords = new double[stringCoords.length];

        for (int i = 0; i < doubleCoords.length; i++) {
            doubleCoords[i] = Double.parseDouble(stringCoords[i]);
        }
        return doubleCoords;
    }

}
