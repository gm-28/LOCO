package com.example.locofx;

import com.example.locofx.Controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class locoApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player_sketch.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(710);
        primaryStage.setMinHeight(535);
        primaryStage.setTitle("LOCO");
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            Controller controller = fxmlLoader.getController();
            controller.pauseSong();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}