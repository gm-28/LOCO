package com.example.locofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class locoApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("player_sketch.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(710);
        primaryStage.setMinHeight(535);
        primaryStage.setTitle("LOCO");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}