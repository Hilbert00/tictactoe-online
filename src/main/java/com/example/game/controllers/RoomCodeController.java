package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.Room;
import com.example.game.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class RoomCodeController {
    @FXML
    public Label roomCode;
    @FXML
    public Label username;

    private final Room room;
    private final User user;

    public RoomCodeController(User user, Room room) {
        this.user = user;
        this.room = room;
    }

    public void initialize() {
        roomCode.setText(room.getRoomCode());
        username.setText(user.getUsername());

        runLoop();
    }

    private void runLoop() {
        if (room.getStatus() != 0) {
            room.setStatus(1);

            try {
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
            return;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    room.update(false);
                    runLoop();
                });
            }
        }, 1000L);
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            room.destroy(user.getId());

            MatchMenuController controller = new MatchMenuController(user);

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/match-menu-view.fxml"));
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) roomCode.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }
}