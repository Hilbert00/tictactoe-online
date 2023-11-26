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


public class SignupController {
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField passwordFieldRepeat;
    @FXML
    public TextField userField;
    @FXML
    public Label loginText;

    @FXML
    protected void onLoginTextClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erro ao carregar o arquivo XML: " + err);
        }
    }

    @FXML
    protected void onSignupButtonClick() {
        Database db = new Database("tictactoe", "root");

        if (db.noConnection()) {
            System.out.println(db.getErrorMessage());
            return;
        }

        String usernameInput = userField.getText();
        String passwordInput = passwordField.getText();
        String passwordInputR = passwordFieldRepeat.getText();

        if (usernameInput.isEmpty() || passwordInput.isEmpty() || passwordInputR.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Velha Online");
            alert.setHeaderText("Preencha todos os campos!");
            alert.setContentText("Para realizar o cadastro é necessário que você preencha todos os campos requisitados.");
            alert.showAndWait();
            return;
        }

        if (!passwordInput.equals(passwordInputR)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Velha Online");
            alert.setHeaderText("As senhas não correspondem!");
            alert.setContentText("As duas senhas inseridas não são iguais.");
            alert.showAndWait();
            return;
        }

        User user = new User(usernameInput, passwordInput, db.getConnection());
        boolean signupStatus = user.signup();

        if (signupStatus) {
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
            error.setHeaderText("Cadastro inválido!");
            error.setContentText("Não foi possível realizar o cadastro.");
            error.showAndWait();
        }
    }
}