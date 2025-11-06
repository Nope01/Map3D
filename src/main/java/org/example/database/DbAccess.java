package org.example.database;

import java.sql.*;

public class DbAccess {

    public static void getNode() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "nope";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
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
