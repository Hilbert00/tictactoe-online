package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class FindMatchController {
    @FXML
    public Label username;
    private final User user;

    public FindMatchController(User user) {
        this.user = user;
    }

    public void initialize() {
        username.setText(user.getUsername());
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