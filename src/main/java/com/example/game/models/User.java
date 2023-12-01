package com.example.game.models;

import java.sql.*;

public class User {
    private int id;
    private final String username;
    private final String password;
    private final Connection connection;

    public User(String username, String password, Connection connection) {
        this.username = username;
        this.password = password;
        this.connection = connection;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Connection getConnection() {
        return connection;
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

    public void updateRanking(boolean win) {
        try {
            String sql = String.format("INSERT INTO leaderboard (users_id_user, wins, matches) VALUES (?, %d, 1);", win ? 1 : 0);

            PreparedStatement req = connection.prepareStatement(sql);
            req.setInt(1, id);

            req.executeUpdate();
        } catch (SQLException err) {
            String sql = String.format("UPDATE leaderboard SET wins = wins + %d, matches = matches + 1 WHERE users_id_user = ?;", win ? 1 : 0);

            try {
                PreparedStatement req = connection.prepareStatement(sql);
                req.setInt(1, id);

                req.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro ao preparar o SQL.");
            }
        }
    }
}
