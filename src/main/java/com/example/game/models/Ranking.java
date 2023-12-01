package com.example.game.models;

import java.sql.*;
import java.util.ArrayList;

public class Ranking {
    private final int wins;
    private final int matches;
    private final String username;

    public Ranking(int wins, int matches, String username) {
        this.wins = wins;
        this.matches = matches;
        this.username = username;
    }

    static public ArrayList<Ranking> getRanking(Connection connection) {
        String sql = "SELECT u.username, l.wins, l.matches FROM leaderboard AS l JOIN users AS u ON l.users_id_user = u.id_user ORDER BY wins DESC;";

        try {
            PreparedStatement req = connection.prepareStatement(sql);

            ResultSet res = req.executeQuery();

            ArrayList<Ranking> ranking = new ArrayList<>();

            while (res.next()) {
                int w = res.getInt("wins");
                int m = res.getInt("matches");
                String u = res.getString("username");

                Ranking r = new Ranking(w, m, u);

                ranking.add(r);
            }

            return ranking;
        } catch (SQLException err) {
            return null;
        }
    }

    public int getWins() {
        return wins;
    }

    public int getMatches() {
        return matches;
    }

    public String getUsername() {
        return username;
    }
}
