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

    public static long[] getNodesFromWay(long id) {
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
        return new long[]{-999};
    }

    public static String[] getTagsFromWay(long id) {
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

    private static long[] sqlStringToIDList(String ids) {
        String[] stringIDs = ids.substring(1, ids.length()-1).split(",");
        long[] longIDs = new long[stringIDs.length];

        for (int i = 0; i < longIDs.length; i++) {
            longIDs[i] = Long.parseLong(stringIDs[i]);
        }
        return longIDs;
    }

    private static String[] sqlStringToTagList(String tags) {
        String[] stringIDs = tags.substring(1, tags.length()-1).split(",");
        return stringIDs;
    }
}
