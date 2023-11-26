package com.example.game.models;

import java.sql.*;

public class User {
    private int id;
    private String username;
    private String password;
    private Connection connection;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Connection connection) {
        this.username = username;
        this.password = password;
        this.connection = connection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean signup() {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?);";

        try {
            PreparedStatement req = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            req.setString(1, username);
            req.setString(2, password);

            req.executeUpdate();

            ResultSet res = req.getGeneratedKeys();

            if (res.next()) {
                this.id = res.getInt(1);
                return true;
            }

            return false;
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
            return false;
        }
    }

    public int login() {
        String sql = "SELECT * FROM users WHERE username = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setString(1, username);

            ResultSet res = req.executeQuery();

            if (res.next()) {
                if (res.getString("password").equals(password)) {
                    this.id = res.getInt("id_user");
                    return 2;
                } else {
                    return 1;
                }
            }

            return 0;
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
            return -1;
        }
    }
}
