package com.example.locofx;

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

        int ret = register(Controller.uti.con);
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
    public static int register(Connection con) throws SQLException {
        if(user.equals("")) return -3;
        if(pass.equals("")) return -2;
        if(!Objects.equals(pass, repass)) return -1;

        String sql = "SELECT * FROM loco_User WHERE username=?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, user);

        ResultSet result = stmt.executeQuery();

        if (result.next()){
            return 0;
        }
        else {
            String SQLinsert = "INSERT INTO loco_User(username, password) " + "VALUES(?,?)";
            PreparedStatement stmt1 = con.prepareStatement(SQLinsert);
            stmt1.setString(1, user);
            stmt1.setString(2, encryptThisString(pass));

            stmt1.executeUpdate();
        }
         return 1;
    }

    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
