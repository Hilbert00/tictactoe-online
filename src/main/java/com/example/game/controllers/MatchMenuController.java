package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.Room;
import com.example.game.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class MatchMenuController {
    @FXML
    public Label username;
    @FXML
    public TextField roomCodeInput;

    private final User user;

    public MatchMenuController(User user) {
        this.user = user;
    }

    public void initialize() {
        username.setText(user.getUsername());
    }

    @FXML
    protected void onCreateButtonClick() {
        try {
            Room room = new Room(user.getConnection(), user.getId());
            RoomCodeController controller = new RoomCodeController(user, room);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/room-code-view.fxml"));
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
    protected void onJoinButtonClick() {
        String roomCode = roomCodeInput.getText();
        if (roomCode.length() != 5) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Velha Online");
            error.setHeaderText("Código inválido!");
            error.setContentText("Utilize um código válido de 5 caracteres.");
            error.showAndWait();
            return;
        }

        try {
            Room room = new Room(user.getConnection(), user.getId(), roomCode);

            if (room.getId() <= 0) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Velha Online");

                if (room.getId() == 0) {
                    error.setHeaderText("Sala cheia!");
                    error.setContentText("Esta sala já conta com dois participantes em jogo.");
                } else {
                    error.setHeaderText("Sala inválida!");
                    error.setContentText("Esta sala não existe mais ou nunca existiu.");
                }

                error.showAndWait();

                return;
            }

            MatchController controller = new MatchController(user, room);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/match-view.fxml"));
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