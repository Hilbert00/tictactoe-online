package com.example.game.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String DATABASE;
    private final String USER;

    private String errorMessage;
    private Connection connection;

    private boolean status = false;

    public Database(String database, String user) {
        this.DATABASE = database;
        this.USER = user;

        createConnection();
    }

    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/" + this.DATABASE, this.USER, "");
            this.status = true;
        } catch (ClassNotFoundException e) {
            this.errorMessage = "Não foi possível localizar o driver!";
        } catch (SQLException e) {
            this.errorMessage = "Houve um erro de SQL!";
        }
    }

    public boolean noConnection() {
        return !status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Connection getConnection() {
        return connection;
    }
}
