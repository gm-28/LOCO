package com.example.locofx.Controllers;

import com.example.locofx.UserDB.Connect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class userControllerTest {
    userController userC = new userController();
    Connect con = new Connect();
    @Test
    @DisplayName("Test: method receives an empty newUsername -> returns -1")
    void changeUsernameInvalid1() throws SQLException {
        assertEquals(-1, userC.changeUsername("", "test", con));
    }

    @Test
    @DisplayName("Test: method receives a newUsername that already exists -> returns 0")
    void changeUsernameInvalid2() throws SQLException {
        assertEquals(0, userC.changeUsername("test", "test", con));
    }

    @Test
    @DisplayName("Test: method receives a newUsername that does not exist -> returns 1")
    void changeUsernameValid() throws SQLException {
        assertEquals(1, userC.changeUsername("test1", "test", con));
        // Next line changes the username back to the original after the test
        userC.changeUsername("test", "test1", con);
    }

}