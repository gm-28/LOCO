package com.example.locofx;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Timer;

@SuppressWarnings("SpellCheckingInspection")
public class Controller implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox hboxT, hboxB, hboxB1, hboxB2;

    @FXML
    private VBox  vboxB, hboxL, vboxR;

    @FXML
    private Label songLabel, dirLabel;

    @FXML
    private TextField directoryField;

    @FXML
    private Region regionL, regionB, regionR;

    @FXML
    private Button playButton, pauseButton, nextButton, previousButton, displayArtists, displayAlbums, displayMusics;

    @FXML
    private Button musicsDisp, artistDisp, albumDisp, playlistButton, logButton;

    @FXML
    private TitledPane titledPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem item1, item2;

    @FXML
    private ListView<String> listDisplay, queueList;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProg;

    @FXML
    private Stage newWindowStage,primaryStage;

    private File file;
    private Library libTest;
    private DirectoryChooser dir;
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private queueClass Queue;
    private String oldItem;
    private boolean firstClick = false;
    int displayN;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titledPane.setText("Songs");
    }

    public void dirChooser(){
        DirectoryChooser dir = new DirectoryChooser();
        primaryStage = (Stage) borderPane.getScene().getWindow();
        file = dir.showDialog(primaryStage);

        if(file != null){
            directoryField.setText(file.getAbsolutePath());
            System.out.println("1");
            libTest = new Library();
            try {
                libTest.getLibrary(file.getAbsolutePath().toString());
            } catch (InvalidDataException | UnsupportedTagException | IOException e) {
                e.printStackTrace();
            }
            System.out.println("3");
            Queue = new queueClass(libTest.musics);
            System.out.println("4");
            Queue.mediaPlayer.setOnEndOfMedia(this::nextSong);
            System.out.println("5");
            songLabel.setText(Queue.list.get(Queue.songNumber).name);
            System.out.println("6");
            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                }
            });
            System.out.println("7");

            int i;
            for (i = 0; i< libTest.musics.size(); i++){
                listDisplay.getItems().add(libTest.musics.get(i).name);
            }
            System.out.println("8");
            updateQueue();
            System.out.println("9");
        }

    }

    public void queueUp() {
        String currDisplay;
        currDisplay = titledPane.getText();

        switch (currDisplay) {
            case "Songs" -> {
                int i = libTest.lookUpSong(listDisplay.getSelectionModel().getSelectedItem());  // MUDAR PARA GETSELECTED ITEMS
                Queue.list.add(libTest.musics.get(i));
                queueList.getItems().add(listDisplay.getSelectionModel().getSelectedItem());
            }
            case "Albums" -> {
                int i2 = libTest.lookUpAlbum(listDisplay.getSelectionModel().getSelectedItem());
                Queue.list.addAll(libTest.albums.get(i2).musics);
                updateQueue();
            }
        }
    }

    public void pushPlay() {
        String currDisplay;
        currDisplay = titledPane.getText();
        switch (currDisplay) {
            case "Songs" -> {
                int index = libTest.lookUpSong(listDisplay.getSelectionModel().getSelectedItem());
                ArrayList<Music> toAdd = new ArrayList<>();
                toAdd.add(libTest.musics.get(index));
                Queue.list = toAdd;
                updateQueue();
                Queue.songNumber = -1;
                nextSong();
            }

            case "Albums" -> {
                int index = libTest.lookUpAlbum(listDisplay.getSelectionModel().getSelectedItem());
                Queue.list = new ArrayList<>(libTest.albums.get(index).musics);
                updateQueue();
                Queue.songNumber = -1;
                nextSong();
            }
        }
    }

    public void listClick() { //NOT WORKING NOT WORKING
        String currDisplay;
        currDisplay = titledPane.getText();

        switch (currDisplay) {
            case "Albums" -> {
                if(firstClick && oldItem.equals(listDisplay.getSelectionModel().getSelectedItem())) {
                    listDisplay.getItems().clear();
                    int i, j;
                    j = libTest.lookUpAlbum(oldItem);
                    ArrayList<String> nameList = new ArrayList<>();
                    System.out.println("AQUI1");
                    int size = libTest.albums.get(j).musics.size();
                    for (i = 0; i< size; i++){
                        nameList.add(libTest.albums.get(j).musics.get(i).name);
                    }
                    System.out.println("AQUI3");
                    listDisplay.getItems().addAll(nameList);
                    titledPane.setText("Songs");
                    firstClick = !firstClick;
                }
                else {
                    firstClick = !firstClick;
                    oldItem = listDisplay.getSelectionModel().getSelectedItem();
                }
            }
        }



    }

    public void handleLogButtonAction(ActionEvent event) {
        try {
            if (newWindowStage == null || !newWindowStage.isShowing()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logScene.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                newWindowStage = new Stage();
                newWindowStage.setTitle("LogIn Window");
                newWindowStage.setScene(new Scene(root1));
                newWindowStage.showAndWait();
            }else {
                newWindowStage.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSong() {
        beginTimer();
        Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        Queue.mediaPlayer.play();
    }
    public void pauseSong() {
        cancelTimer();
        Queue.mediaPlayer.pause();
    }
    public void nextSong() {
        Queue.mediaPlayer.stop();
        if(running){
            cancelTimer();
        }
        Queue.setNext();
        songLabel.setText(Queue.list.get(Queue.songNumber).name);
        Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        playSong();
        Queue.mediaPlayer.setOnEndOfMedia(this::nextSong);

    }
    public void previousSong() {
        Queue.mediaPlayer.stop();
        if(running){
            cancelTimer();
        }

        Queue.setPrevious();
        songLabel.setText(Queue.list.get(Queue.songNumber).name);
        Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        playSong();
        Queue.mediaPlayer.setOnEndOfMedia(this::nextSong);
    }

    public void beginTimer() {
        timer = new java.util.Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = Queue.mediaPlayer.getCurrentTime().toMillis();
                double end = Queue.media.getDuration().toMillis();
                songProg.setProgress(current/end);
                if(current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }

    public void displayMusics() {
        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.musics.size(); i++) {
            listDisplay.getItems().add(libTest.musics.get(i).name);
        }
        titledPane.setText("Songs");
    }

    public void setDisplayArtists() {

        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.artists.size(); i++) {
            listDisplay.getItems().add(libTest.artists.get(i).name);
        }
        titledPane.setText("Album Artist");
    }

    public void displayAlbums() {

        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.albums.size(); i++){
            listDisplay.getItems().add(libTest.albums.get(i).title);
        }
        titledPane.setText("Albums");

    }

    private void updateQueue() {
        int i;
        queueList.getItems().clear();
        for (i = 0; i< Queue.list.size(); i++){
            queueList.getItems().add(Queue.list.get(i).name);
        }
    }
}





