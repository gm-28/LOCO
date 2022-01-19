package com.example.locofx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class regSceneController {

    @FXML
    private Button SignUpButton;

    @FXML
    private Label statLabel;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private PasswordField repassField;

    private static String user;
    private static String pass;
    private static String repass;

    public void handleSignUpButton() throws SQLException {
        user = userField.getText();
        pass = passField.getText();
        repass = repassField.getText();

        int ret = register(Controller.connect.con);
        switch (ret) {
            case -3 -> {
                statLabel.setText("Please insert a username");
            }
            case -2 -> {
                statLabel.setText("Please insert a password");
            }
            case -1 -> {
                statLabel.setText("Passwords don't match");
            }
            case 0 -> {
                statLabel.setText("Username already taken");
            }
            case 1 -> {
                Stage thisStage = (Stage)  SignUpButton.getScene().getWindow();
                thisStage.close();

            }
        }
    }
    private static int register(Connection con) throws SQLException {
        if(user.equals("")) return -3;
        if(pass.equals("")) return -2;
        if(!Objects.equals(pass, repass)) return -1;

        String sql = "SELECT * FROM loco_User WHERE username=?";
        ResultSet result = Controller.connect.sendStatemant(sql,user,null,0);

        if (result.next()){
            return 0;
        }else{
            String SQLinsert = "INSERT INTO loco_User(username, password) VALUES(?,?)";

            Controller.connect.sendStatemant(SQLinsert, user, encryptThisString(pass), 1);

            PreparedStatement stmt1 = con.prepareStatement(SQLinsert);
            stmt1.setString(1, user);
            stmt1.setString(2, encryptThisString(pass));
            stmt1.executeUpdate();
        }
        return 1;
    }

    private static String encryptThisString(String input)
    {
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
