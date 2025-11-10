package org.example.database;

import java.sql.*;

public class Node {

    public static void getNode() {
        try (Connection connection = DriverManager.getConnection(DbAccess.url, DbAccess.user, DbAccess.password)) {
            System.out.println("Connected to db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM node WHERE id = 12642563680");
            while (resultSet.next()) {
                System.out.println("Results got!");
                System.out.println(resultSet.getString(2));
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
