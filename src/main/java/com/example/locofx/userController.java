package com.example.locofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class userController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button setButton, logOffButton;
    @FXML
    private TextField userField;
    @FXML
    private CheckBox changeU;
    @FXML
    private CheckBox changeP;
    @FXML
    private Label statLabel;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField repassField;
    @FXML
    private PasswordField newpassField;
    @FXML
    public ListView<String> topMusics;
    @FXML
    public ListView<String> topAlbums;
    @FXML
    public ListView<String> topArtists;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    public ImageView imageView;

    private boolean setShowing;
    private Parent root;

    public void handleSetButton() throws SQLException, IOException {
        root = null;
        if (!setShowing) {
            try {
                root = FXMLLoader.load(getClass().getResource("settingScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(root);
            setShowing = true;
            setButton.setText("Statistics");
        } else {
            setShowing = false;
            borderPane.setCenter(anchorPane);
            setButton.setText("Settings");
        }
    }

    public void handleLogOutButton() {
        Controller.uti.username="";
        Controller.logged=false;
        Stage thisStage = (Stage)  logOffButton.getScene().getWindow();
        thisStage.close();
    }

    public void handleChangeButton(ActionEvent event) throws SQLException {
        Connection con = Controller.uti.con;

        String sql = "SELECT * FROM loco_User WHERE username=?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, Controller.uti.username);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            String password = result.getString(2);

            if (Objects.equals(encryptThisString(passField.getText()), password)) {
                if (changeU.isSelected()) {
                    String newUsername = userField.getText();

                    int ret = changeUsername(con, newUsername);
                    switch (ret) {
                        case 0 -> {
                            statLabel.setText("Username already taken");
                        }
                        case 1 -> {
                            statLabel.setText("Username changed");
                            Controller.uti.username = userField.getText();
                        }
                    }
                }
                else if (changeP.isSelected()) {
                    int ret = changePassword(con, newpassField.getText(), repassField.getText());
                    switch (ret) {
                        case 0 -> {
                            statLabel.setText("Passwords don't match");
                        }
                        case 1 -> {
                            statLabel.setText("Password changed");
                            Controller.uti.username = userField.getText();
                        }
                    }
                }
            } else statLabel.setText("Wrong Password");
        }
    }

    public int changeUsername (Connection con, String newUsername) throws SQLException {
        String sql = "SELECT * FROM loco_User WHERE username=?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, newUsername);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            return 0;
        } else {
            sql = "Update loco_user SET username=? WHERE username=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, newUsername);
            stmt.setString(2, Controller.uti.username);
            stmt.executeUpdate();
        }
        return 1;
    }

    public int changePassword (Connection con, String new_password, String repass) throws SQLException {
        if(Objects.equals(new_password, repass)){
            new_password = encryptThisString(new_password);

            String sql = "Update loco_user SET password=? WHERE username=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, new_password);
            stmt.setString(2, Controller.uti.username);
            stmt.executeUpdate();
            return 1;
        } return 0;
    }

    public void handleCheckU(){
        if(changeP.isSelected()){
            changeP.setSelected(false);
        }
    }

    public void handleCheckP(){
        if(changeU.isSelected()){
            changeU.setSelected(false);;
        }
    }

    public static String encryptThisString (String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
