package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class StartController {
    @FXML
    public Label username;
    private final User user;

    public StartController(User user) {
        this.user = user;
    }

    public void initialize() {
        username.setText(user.getUsername());
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }

    @FXML
    protected void onPlayButtonClick() {
        try {
            MatchMenuController controller = new MatchMenuController(user);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/match-menu-view.fxml"));
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }

    @FXML
    protected void onRankingButtonClick() {
        try {
            RankingController controller = new RankingController(user);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/ranking-view.fxml"));
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