module com.example.locofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mp3agic;
    requires java.desktop;
    requires javafx.media;


    opens com.example.locofx to javafx.fxml;
    exports com.example.locofx;
}