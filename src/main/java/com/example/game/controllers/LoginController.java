package com.example.game.controllers;

import com.example.game.Application;
import com.example.game.models.Database;
import com.example.game.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userField;
    @FXML
    public Label signupText;

    @FXML
    protected void onSignupTextClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/signup-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }

    @FXML
    protected void onLoginButtonClick() {
        Database db = new Database("tictactoe", "root");

        if (db.noConnection()) {
            System.out.println(db.getErrorMessage());
            return;
        }

        String usernameInput = userField.getText();
        String passwordInput = passwordField.getText();

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Velha Online");
            alert.setHeaderText("Preencha todos os campos!");
            alert.setContentText("Para realizar o login é necessário que você preencha todos os campos requisitados.");
            alert.showAndWait();
            return;
        }

        User user = new User(usernameInput, passwordInput, db.getConnection());
        int loginStatus = user.login();

        if (loginStatus == 2) {
            try {
                StartController controller = new StartController(user);

                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/start-view.fxml"));
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 450);
                Stage stage = (Stage) userField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException err) {
                System.out.println("Erro ao carregar o arquivo XML: " + err);
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Velha Online");
            error.setHeaderText("Login inválido!");

            if (loginStatus == 0) {
                error.setContentText("Não há nenhum usuário cadastrado com esse nome.");
            } else if (loginStatus == 1) {
                error.setContentText("A senha digitada não corresponde à deste usuário.");
            } else {
                error.setContentText("Um erro inesperado ocorreu.");
            }

            error.showAndWait();
        }
    }
}