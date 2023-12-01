package com.example.game.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.*;

public class Room {
    private int id;
    private int[] pos;
    private String roomCode;
    private final int userID;
    private final int playerNumber;
    private final Connection connection;

    private int status;
//  0 = Waiting for another player
//  1 = Player One's turn
//  2 = Player Two's turn
//  3 = Player Two joined and is waiting for confirmation

    public Room(Connection connection, int userID) {
        this.status = 0;
        this.playerNumber = 1;
        this.userID = userID;
        this.pos = new int[9];
        this.connection = connection;

        String chars = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ123456789";
        StringBuilder rc = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            int index = (int) Math.floor(Math.random() * chars.length());

            rc.append(chars.charAt(index));
        }

        String roomCode = rc.toString();
        String sql = "INSERT INTO room (code) VALUES (?);";

        try {
            PreparedStatement req = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            req.setString(1, roomCode);

            req.executeUpdate();

            ResultSet res = req.getGeneratedKeys();

            if (res.next()) {
                this.id = res.getInt(1);
                this.roomCode = roomCode;
            }
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }

        sql = "INSERT INTO users_is_in_room VALUES (?, ?);";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setInt(1, this.userID);
            req.setInt(2, id);

            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }
    }

    public Room(Connection connection, int userID, String roomCode) {
        this.userID = userID;
        this.roomCode = roomCode;
        this.connection = connection;
        this.playerNumber = 2;

        update(true);
    }

    public String getRoomCode() {
        return roomCode;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;

        String sql = "UPDATE room SET status = ? WHERE id_room = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);

            req.setInt(1, status);
            req.setInt(2, id);
            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }
    }

    public int[] getPos() {
        return pos;
    }

    public int getPos(int n) {
        return pos[n - 1];
    }

    public void setPos(int n, int value) {
        this.pos[n - 1] = value;

        String sql = String.format("UPDATE room SET pos%d = ? WHERE id_room = ?;", n);

        try {
            PreparedStatement req = connection.prepareStatement(sql);

            req.setInt(1, status);
            req.setInt(2, id);
            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean canJoin() {
        String sql = "SELECT COUNT(*) AS in_room FROM users_is_in_room WHERE room_id_room = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setInt(1, id);

            ResultSet res = req.executeQuery();

            res.next();

            int in_room = res.getInt("in_room");

            return in_room <= 1;
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
            return false;
        }
    }

    public void join() {
        String sql = "INSERT INTO users_is_in_room VALUES (?, ?);";

        try {
            PreparedStatement req = connection.prepareStatement(sql);

            req.setInt(1, userID);
            req.setInt(2, id);
            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }

        sql = "UPDATE room SET status = ? WHERE id_room = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);

            req.setInt(1, 3);
            req.setInt(2, id);
            req.executeUpdate();

            this.status = 3;
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }
    }

    public void update(boolean join) {
        String sql = "SELECT * FROM room WHERE BINARY code = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setString(1, roomCode);

            ResultSet res = req.executeQuery();

            if (res.next()) {
                this.id = res.getInt("id_room");
                this.status = res.getInt("status");

                int[] pos = new int[9];

                for (int i = 0; i < pos.length; i++) {
                    pos[i] = res.getInt("pos" + (i + 1));
                }

                this.pos = pos;
            } else {
                this.id = -1;
            }
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }

        if (!join || id <= 0) return;

        if (canJoin()) {
            join();
        } else {
            id = 0;
        }
    }

    public void destroy(int userID) {
        String sql = "DELETE FROM users_is_in_room WHERE room_id_room = ? AND users_id_user = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setInt(1, id);
            req.setInt(2, userID);

            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }

        sql = "DELETE FROM room WHERE id_room = ?;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);
            req.setInt(1, id);

            req.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erro ao preparar o SQL.");
        }
    }
}
