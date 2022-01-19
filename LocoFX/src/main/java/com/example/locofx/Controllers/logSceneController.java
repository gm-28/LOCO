package com.example.locofx.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class logSceneController {
    @FXML
    private Button logIN;
    @FXML
    private Label statLabel;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String user;
    private static String pass;

    public void handleSignUpButtonAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("regScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(Boolean.FALSE);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleLoginButton(ActionEvent event) throws SQLException {
        user = userField.getText();
        pass = passField.getText();

        int ret = login(Controller.connect.con);
        switch (ret){
            case -1 ->{
                statLabel.setText("Username not found");
            }
            case 0 ->{
                statLabel.setText("Wrong Password");
            }
            case 1 ->{
                statLabel.setText("good");
                Controller.uti.username = userField.getText();
                Stage thisStage = (Stage)  logIN.getScene().getWindow();
                thisStage.close();
            }
        }
    }

    private static int login(Connection con) throws SQLException {
        String sql = "SELECT * FROM loco_user WHERE username=?";
        ResultSet result = Controller.connect.sendStatemant(sql,user,null,0);

        if (result.next()) {
            String password = result.getString(2);

            if (Objects.equals(encryptThisString(pass), password)) {
                return 1;
            } else return 0;
        } else return -1;
    }

    private static String encryptThisString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
