package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.Ranking;
import com.example.game.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RankingController {
    @FXML
    public Label username;
    @FXML
    public VBox list;

    private final User user;

    public RankingController(User user) {
        this.user = user;
    }

    public void initialize() {
        username.setText(user.getUsername());

        ArrayList<Ranking> ranking = Ranking.getRanking(user.getConnection());

        for (int i = 0; i < Objects.requireNonNull(ranking).size(); i++) {
            Label label = new Label();
            Ranking r = ranking.get(i);

            label.setText(String.format("#%d | %s | %d vitórias | %d partidas", i + 1, r.getUsername(), r.getWins(), r.getMatches()));
            label.setStyle("-fx-font-size: 25;");
            list.getChildren().add(label);
        }
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            StartController controller = new StartController(user);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/start-view.fxml"));
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }
}