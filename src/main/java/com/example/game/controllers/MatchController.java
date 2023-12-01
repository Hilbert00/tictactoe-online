package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.Room;
import com.example.game.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


public class MatchController {
    @FXML
    public Label username;
    @FXML
    public Label title;

    @FXML
    public ImageView pos1;
    @FXML
    public ImageView pos2;
    @FXML
    public ImageView pos3;
    @FXML
    public ImageView pos4;
    @FXML
    public ImageView pos5;
    @FXML
    public ImageView pos6;
    @FXML
    public ImageView pos7;
    @FXML
    public ImageView pos8;
    @FXML
    public ImageView pos9;


    private final Room room;
    private final User user;
    private boolean exited = false;
    private boolean canPlay = false;
    private boolean gameRunning = true;

    public MatchController(User user, Room room) {
        this.user = user;
        this.room = room;
    }

    public void initialize() {
        username.setText(user.getUsername());

        runLoop();
    }

    private void setTitle() {
        switch (room.getStatus()) {
            case 1, 2-> {
                if (room.getPlayerNumber() == room.getStatus()) {
                    title.setText("Sua Vez");
                    canPlay = true;
                } else {
                    canPlay = false;
                    title.setText("Vez do Adversário");
                }
            }

            case 0, 3 -> title.setText("Aguarde...");
        }
    }

    private void runLoop() {
        setTitle();
        updateVisuals();
        if (exited) return;

        if (room.getStatus() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Velha Online");
            error.setHeaderText("Desistência!");
            error.setContentText("O outro jogador deixou a sala.");

            exit();
            error.showAndWait();

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

       if (gameRunning) checkWinner();
    }

    private void updatePos(int n) {
        if (room.getPos(n) != 0 || !canPlay) return;

        canPlay = false;
        room.setPos(n, room.getPlayerNumber());

        if (room.getPlayerNumber() == 1) room.setStatus(2);
        else room.setStatus(1);
    }

    private void updateVisuals() {
        ImageView[] posV = { pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9 };

        for (int i = 0; i < posV.length; i++) {
            if (posV[i].getImage() != null) continue;

            if (room.getPos(i + 1) == 1) {
                Image image = new Image(Objects.requireNonNull(Application.class.getResource("images/x.png")).toString());
                posV[i].setImage(image);
            } else if (room.getPos(i + 1) == 2) {
                Image image = new Image(Objects.requireNonNull(Application.class.getResource("images/o.png")).toString());
                posV[i].setImage(image);
            }
        }
    }

    private void checkWinner() {
        boolean spacesLeft = false;
        int[] iniBoard = room.getPos();

        for (int p : iniBoard) {
            if (p == 0) {
                spacesLeft = true;
                break;
            }
        }

        if (!spacesLeft) {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Velha Online");
            alert.setHeaderText("Empate!");
            alert.setContentText("Você empatou nesta partida.");

            gameRunning = false;
            user.updateRanking(false);
            alert.showAndWait();
            exit();

            return;
        }

        for (int i = 0; i < 8; i++) {
            String line = null;
            String[] board = new String[9];

            for (int e = 0; e < iniBoard.length; e++) {
                if (iniBoard[e] == 1) board[e] = "X";
                else if (iniBoard[e] == 2) board[e] = "O";
                else board[e] = "";
            }

            line = switch (i) {
                case 0 -> board[0] + board[1] + board[2];
                case 1 -> board[3] + board[4] + board[5];
                case 2 -> board[6] + board[7] + board[8];
                case 3 -> board[0] + board[3] + board[6];
                case 4 -> board[1] + board[4] + board[7];
                case 5 -> board[2] + board[5] + board[8];
                case 6 -> board[0] + board[4] + board[8];
                case 7 -> board[2] + board[4] + board[6];
                default -> line;
            };

            if (line.equals("XXX")) {
                Alert alert;

                if (room.getPlayerNumber() == 1) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Velha Online");
                    alert.setHeaderText("Vitória!");
                    alert.setContentText("Você venceu esta partida.");

                    user.updateRanking(true);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Velha Online");
                    alert.setHeaderText("Derrota!");
                    alert.setContentText("Você perdeu esta partida.");

                    user.updateRanking(false);
                }

                gameRunning = false;
                alert.showAndWait();
                exit();

                return;
            } else if (line.equals("OOO")) {
                Alert alert;

                if (room.getPlayerNumber() == 2) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Velha Online");
                    alert.setHeaderText("Vitória!");
                    alert.setContentText("Você venceu esta partida.");

                    user.updateRanking(true);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Velha Online");
                    alert.setHeaderText("Derrota!");
                    alert.setContentText("Você perdeu esta partida.");

                    user.updateRanking(false);
                }

                gameRunning = false;
                alert.showAndWait();
                exit();
                return;
            }
        }
    }

    private void exit() {
        try {
            exited = true;

            if (gameRunning) room.setStatus(0);
            room.destroy(user.getId());

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
    protected void updatePosOne() {
        updatePos(1);
    }

    @FXML
    protected void updatePosTwo() {
        updatePos(2);
    }

    @FXML
    protected void updatePosThree() {
        updatePos(3);
    }

    @FXML
    protected void updatePosFour() {
        updatePos(4);
    }

    @FXML
    protected void updatePosFive() {
        updatePos(5);
    }

    @FXML
    protected void updatePosSix() {
        updatePos(6);
    }

    @FXML
    protected void updatePosSeven() {
        updatePos(7);
    }

    @FXML
    protected void updatePosEight() {
        updatePos(8);
    }

    @FXML
    protected void updatePosNine() {
        updatePos(9);
    }

    @FXML
    protected void onBackButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Velha Online");
        alert.setHeaderText("Desistir!");
        alert.setContentText("Tem certeza que deseja desistir?");
        Optional<ButtonType> res = alert.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            exit();
        }
    }
}