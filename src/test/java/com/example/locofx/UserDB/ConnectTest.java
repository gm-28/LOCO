package com.example.locofx.UserDB;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectTest {

    Connect con=new Connect();

    @Test
    @DisplayName("Test: method sends a valid statement (case 0) -> returns ResulSet from database")
    void sendValidStatement0() throws SQLException {
        assertNotNull(con.sendStatement("SELECT * FROM loco_user WHERE username=?", null,null, 0));
    }

    @Test
    @DisplayName("Test: method sends a valid statement (case 1) -> returns null")
    void sendValidStatement1() throws SQLException {
        assertNull(con.sendStatement("Update loco_user SET username=? WHERE username=?", "test","test", 1));
    }

    @Test
    @DisplayName("Test: method sends a valid statement (case 2) -> returns ResulSet from database")
    void sendValidStatement2() throws SQLException {
        assertNotNull(con.sendStatement("SELECT * FROM stats WHERE username=? AND music=?", null,null, 2));
    }

    @Test
    @DisplayName("Test: method sends an invalid statement (case 0) -> gets a specific SQL exception")
    void sendInvalidStatement0() {
        assertThrows(org.postgresql.util.PSQLException.class,
                () -> {
            con.sendStatement("", null,null, 0);
                });
    }

    @Test
    @DisplayName("Test: method sends an invalid statement (case 1) -> gets a specific SQL exception")
    void sendInvalidStatement1() {
        assertThrows(org.postgresql.util.PSQLException.class,
                () -> {
                    con.sendStatement("", null,null, 1);
                });
    }

    @Test
    @DisplayName("Test: method sends an invalid statement (case 2) -> gets a specific SQL exception")
    void sendInvalidStatement2() {
        assertThrows(org.postgresql.util.PSQLException.class,
                () -> {
                    con.sendStatement("", null,null, 2);
                });
    }
}