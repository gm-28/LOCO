module com.example.locofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mp3agic;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;


    opens com.example.locofx to javafx.fxml;
    exports com.example.locofx;
    exports com.example.locofx.Organizer;
    opens com.example.locofx.Organizer to javafx.fxml;
    exports com.example.locofx.UserDB;
    opens com.example.locofx.UserDB to javafx.fxml;
    exports com.example.locofx.Controllers;
    opens com.example.locofx.Controllers to javafx.fxml;
}