package com.example.locofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class logSceneController {

    @FXML
    private Button logIN;

    @FXML
    private Button regButton;

    @FXML
    private Label statLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void handleSignUpButtonAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("regScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
