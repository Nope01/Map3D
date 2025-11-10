package org.example.database;

import java.sql.*;

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

    public static String[] getNodesFromWay(String id) {
        try (Connection connection = DbAccess.connectToDB()){
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM way WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            System.out.println("Results got!");
            return sqlStringToIDList(resultSet.getString(2));

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());;
        }
        return new String[]{"-999"};
    }

    public static String[] getTagsFromWay(String id) {
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
        return new String[]{"-999"};
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

    private static String[] sqlStringToIDList(String ids) {
        String[] stringIDs = ids.substring(1, ids.length()-1).split(",");
        return stringIDs;
//        long[] longIDs = new long[stringIDs.length];
//
//        for (int i = 0; i < longIDs.length; i++) {
//            longIDs[i] = Long.parseLong(stringIDs[i]);
//        }
//        return longIDs;
    }

    private static String[] sqlStringToTagList(String tags) {
        String[] stringIDs = tags.substring(1, tags.length()-1).split(",");
        return stringIDs;
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
