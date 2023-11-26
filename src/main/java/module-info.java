module com.example.game {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.game to javafx.fxml;
    exports com.example.game;
    exports com.example.game.controllers;
    exports com.example.game.models;
    opens com.example.game.controllers to javafx.fxml;
}