package com.example.locofx.Controllers;

import com.example.locofx.Organizer.Library;
import com.example.locofx.Organizer.Music;
import com.example.locofx.UserDB.Connect;
import com.example.locofx.UserDB.User;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Timer;

@SuppressWarnings("SpellCheckingInspection")
public class Controller implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label songLabel;
    @FXML
    private TextField directoryField;
    @FXML
    private Button logButton;
    @FXML
    private Label currLabel;
    @FXML
    private ListView<String> listDisplay;
    @FXML
    private ListView<String> queueList;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProg;
    @FXML
    private Stage newWindowStage,primaryStage;
    @FXML
    private ImageView imageView;

    private File file;
    private Library libTest;
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private queueClass Queue;
    private String oldItem;
    private boolean firstClick = false;
    private int displayN; //1 - Songs || 2 - Albums || 3 - Artists
    public static User uti;
    private boolean listened = false;
    public static Connect connect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currLabel.setText("Songs");
        displayN = 1;
        uti = new User();
        connect = new Connect();
    }

    public void dirGet() throws SQLException {
        String sql = "SELECT directory FROM loco_user WHERE username=?";
        ResultSet result = connect.sendStatemant(sql, uti.username, null,0);

        if (result.next()) {
            if (result.getString(1)==null) {
                String sql2 = "Update loco_user SET directory=? WHERE username=?";
                connect.sendStatemant(sql2, uti.dir, uti.username, 1);
            }else
                uti.dir=result.getString(1);
        }
    }

    public void dirChooser() throws SQLException {
        DirectoryChooser dir = new DirectoryChooser();
        primaryStage = (Stage) borderPane.getScene().getWindow();
        file = dir.showDialog(primaryStage);
        if(file == null) return;
        uti.dir = file.getAbsolutePath();

        if (uti.username!=null) {
            String sql = "Update loco_user SET directory=? WHERE username=?";
            connect.sendStatemant(sql,uti.dir, uti.username, 1);
        }
        setLibrary();
    }

    private void setLibrary(){
        boolean done =false;
        if(uti.dir!=null){
            directoryField.setText(uti.dir);
            libTest = new Library();

            try {
                done = libTest.getLibrary(uti.dir);
            } catch (InvalidDataException | UnsupportedTagException | IOException e) {
                e.printStackTrace();
            }

            if(done) {
                if (running) {
                    pauseSong();
                }

                Queue = new queueClass(libTest.musics);
                Queue.mediaPlayer.setOnEndOfMedia(() -> {
                    try {
                        nextSong();
                    } catch (InvalidDataException | UnsupportedTagException | IOException e) {
                        e.printStackTrace();
                    }
                });

                volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                    }
                });
                displayMusics();
                updateQueue();
            }
        }
    }

    public void queueUp() {
        switch (displayN) {
            case 1 -> {
                String str =listDisplay.getSelectionModel().getSelectedItem();
                if (str==null) return;
                String[] srt1 = str.split(" - ");
                int i = libTest.lookUpSong(srt1[1]);  // MUDAR PARA GETSELECTED ITEMS
                Queue.list.add(libTest.musics.get(i));
                updateQueue();
            }
            case 2 -> {
                int i2 = libTest.lookUpAlbum(listDisplay.getSelectionModel().getSelectedItem());
                Queue.list.addAll(libTest.albums.get(i2).musics);
                updateQueue();
            }
        }
    }

    public void pushPlay() throws InvalidDataException, UnsupportedTagException, IOException {;
        switch (displayN) {
            case 1 -> {
                String str =listDisplay.getSelectionModel().getSelectedItem();
                if (str==null) return;
                String[] str1 = str.split(" - ");
                int index = libTest.lookUpSong(str1[1]);
                ArrayList<Music> toAdd = new ArrayList<>();
                toAdd.add(libTest.musics.get(index));
                Queue.list = toAdd;
                updateQueue();
                Queue.songNumber = -1;
                nextSong();
            }

            case 2 -> {
                int index = libTest.lookUpAlbum(listDisplay.getSelectionModel().getSelectedItem());
                Queue.list = new ArrayList<>(libTest.albums.get(index).musics);
                updateQueue();
                Queue.songNumber = -1;
                nextSong();
            }
        }
    }

    public void listClick() throws InvalidDataException, UnsupportedTagException, IOException {
        if(listDisplay.getSelectionModel().getSelectedItem()!= null) {
            if (firstClick && oldItem.equals(listDisplay.getSelectionModel().getSelectedItem())) {
                switch (displayN) {
                    case 1 -> {
                        pushPlay();
                        firstClick = !firstClick;
                    }
                    case 2 -> {
                        listDisplay.getItems().clear();
                        int i, j;
                        j = libTest.lookUpAlbum(oldItem);
                        ArrayList<String> nameList = new ArrayList<>();
                        int size = libTest.albums.get(j).musics.size();
                        for (i = 0; i < size; i++) {
                            nameList.add(libTest.albums.get(j).musics.get(i).artist+" - "+libTest.albums.get(j).musics.get(i).name);
                        }
                        listDisplay.getItems().addAll(nameList);
                        currLabel.setText(oldItem);
                        displayN = 1;
                        firstClick = !firstClick;
                    }

                    case 3 -> {
                        listDisplay.getItems().clear();
                        int i, j;
                        j = libTest.artist_exists(oldItem);
                        ArrayList<String> nameList = new ArrayList<>();
                        int size = libTest.artists.get(j).albums.size();
                        for (i = 0; i < size; i++) {
                            nameList.add(libTest.artists.get(j).albums.get(i).title);
                        }
                        listDisplay.getItems().addAll(nameList);
                        currLabel.setText(oldItem);
                        displayN = 2;
                        firstClick = !firstClick;
                    }
                }
            } else{
                firstClick = !firstClick;
                oldItem = listDisplay.getSelectionModel().getSelectedItem();
            }
        }
    }

    public void handleLogButtonAction() {
        if(uti.username == null) {
            try {
                if ( newWindowStage == null ||!newWindowStage.isShowing()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logScene.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    logSceneController logControl = fxmlLoader.getController();

                    newWindowStage = new Stage();
                    newWindowStage.resizableProperty().setValue(Boolean.FALSE);
                    newWindowStage.setTitle("LOGIN");
                    newWindowStage.setScene(new Scene(root1));
                    newWindowStage.showAndWait();

                    if (uti.username != null) {
                        logButton.setText(uti.username);
                        dirGet();
                        setLibrary();
                        String currPath = Queue.currFile.getAbsolutePath();
                        setSong(imageView,currPath);
                    }
                }else{
                    newWindowStage.toFront();
                }
            } catch (IOException | SQLException | InvalidDataException | UnsupportedTagException e) {
                e.printStackTrace();
            }
        }else {
            try {
                if (!newWindowStage.isShowing()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userScene.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();

                    userController userController = fxmlLoader.getController();
                    calcStats(userController);

                    newWindowStage = new Stage();
                    newWindowStage.resizableProperty().setValue(Boolean.FALSE);
                    newWindowStage.setTitle("User");
                    newWindowStage.setScene(new Scene(root1));
                    newWindowStage.showAndWait();

                    if(uti.username == null){
                        logButton.setText("LOGIN");
                    }else
                        logButton.setText(uti.username);
                } else {
                    newWindowStage.toFront();
                }
            } catch (IOException | SQLException | InvalidDataException | UnsupportedTagException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSong() throws InvalidDataException, UnsupportedTagException, IOException {
        if(!running && Queue !=null){
            beginTimer();
            Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            Queue.mediaPlayer.play();
        }
    }

    public void pauseSong() {
        if (running) {
            cancelTimer();
            Queue.mediaPlayer.pause();
        }
    }

    public void nextSong() throws InvalidDataException, UnsupportedTagException, IOException {
        if(Queue != null) {
            Queue.mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            Queue.setNext();
            Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            playSong();

            Queue.mediaPlayer.setOnEndOfMedia(() -> {
                try {
                    nextSong();
                } catch (InvalidDataException | UnsupportedTagException | IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void previousSong() throws InvalidDataException, UnsupportedTagException, IOException {
        if(Queue != null) {
            Queue.mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }

            Queue.setPrevious();



            Queue.mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            playSong();
            Queue.mediaPlayer.setOnEndOfMedia(() -> {
                try {
                    nextSong();
                } catch (InvalidDataException | UnsupportedTagException | IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void beginTimer() throws InvalidDataException, UnsupportedTagException, IOException {
        String currPath = Queue.currFile.getAbsolutePath();

        setSong(imageView, currPath);

        listened = false;
        timer = new java.util.Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = Queue.mediaPlayer.getCurrentTime().toMillis();
                double end = Queue.media.getDuration().toMillis();
                songProg.setProgress(current / end);

                if (current / end == 1) {
                    cancelTimer();
                }

                if (current / end > 0.3 && !listened) {
                    if (uti.username != null) {
                        try {
                            setUpData();
                        } catch (InvalidDataException | UnsupportedTagException | IOException | SQLException e) {
                            e.printStackTrace();
                        }
                        listened = true;
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private void cancelTimer(){
        running = false;
        timer.cancel();
    }

    public void displayMusics() {
        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.musics.size(); i++) {
            listDisplay.getItems().add(libTest.musics.get(i).artist+" - "+libTest.musics.get(i).name);
        }
        currLabel.setText("Songs");
        displayN = 1;
    }

    public void setDisplayArtists() {
        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.artists.size(); i++) {
            listDisplay.getItems().add(libTest.artists.get(i).name);
        }
        currLabel.setText("Artist");
        displayN = 3;
    }

    public void displayAlbums() {
        listDisplay.getItems().clear();
        int i;
        for (i = 0; i< libTest.albums.size(); i++){
            listDisplay.getItems().add(libTest.albums.get(i).title);
        }

        currLabel.setText("Albums");
        displayN = 2;
    }

    private void updateQueue() {
        int i;
        queueList.getItems().clear();
        for (i = 0; i< Queue.list.size(); i++){
            queueList.getItems().add( i+1 + " - "+ Queue.list.get(i).name);
        }
    }

    private void setUpData() throws InvalidDataException, UnsupportedTagException, IOException, SQLException {
        String currPath = Queue.list.get(Queue.songNumber).filePath;

        Mp3File curr_mp3 = new Mp3File(currPath);
        if (curr_mp3.hasId3v2Tag()) {
            ID3v2 id3v2Tag = curr_mp3.getId3v2Tag(); //verifica se tem a tag e insere a faixa na lista consoante o track number
            String curr_alb = id3v2Tag.getAlbum();
            String curr_artist = id3v2Tag.getArtist();
            String curr_song = id3v2Tag.getTitle();

            String sql = "SELECT * FROM stats WHERE username=? AND music=?";
            ResultSet result = connect.sendStatemant(sql, uti.username, curr_song,2);

            if (result.next()) {
                sql = "UPDATE stats SET counter=counter+1 WHERE username=? AND music=?";
                result = connect.sendStatemant(sql,uti.username,curr_song,1);
            }
            else {
                String SQLinsert = "INSERT INTO stats(username, music, album, artist, counter) VALUES(?,?,?,?,?)";

                PreparedStatement stmt1 = connect.con.prepareStatement(SQLinsert);
                stmt1.setString(1, uti.username);
                stmt1.setString(2, curr_song);
                stmt1.setString(3, curr_alb);
                stmt1.setString(4, curr_artist);
                stmt1.setInt(5, 1);

                stmt1.executeUpdate();
            }
        }

    }

    private void calcStats(userController userController) throws SQLException, InvalidDataException, UnsupportedTagException, IOException {
        boolean k = false;
        String temp = null;
        userController.topMusics.getItems().clear();
        userController.topAlbums.getItems().clear();
        userController.topArtists.getItems().clear();

        int j=1;
        String sql = "SELECT music, counter FROM stats WHERE username=? ORDER BY counter DESC LIMIT 5";
        ResultSet result = connect.sendStatemant(sql, uti.username, null,0);
        while(result.next()) {
            String columnValue = result.getString(1);
            int i = result.getInt(2);
            userController.topMusics.getItems().add( j + " - "+ columnValue + ": " +i );
            j++;
        }
        sql = "SELECT album,sum(counter) FROM stats WHERE username=? GROUP BY album ORDER BY sum(counter) desc limit 5";
        result = connect.sendStatemant(sql, uti.username, null,0);
        j=1;
        while(result.next()) {
            String columnValue = result.getString(1);
            int i = result.getInt(2);
            userController.topAlbums.getItems().add( j + " - "+ columnValue + ": " +i );
            j++;

            if(!k) {
                temp = columnValue;
                k = true;
            }
        }

        sql = "SELECT artist,sum(counter) FROM stats WHERE username=? GROUP BY artist ORDER BY sum(counter) desc limit 5";
        result = connect.sendStatemant(sql, uti.username, null,0);

        j=1;
        while(result.next()) {
            String columnValue = result.getString(1);
            int i = result.getInt(2);
            userController.topArtists.getItems().add( j + " - "+ columnValue + ": " +i );
            j++;
        }

        if(uti.dir!=null) {
            if (temp == null) return;
            int index = libTest.lookUpAlbum(temp);
            if (index >= 0) {
                String currPath = libTest.albums.get(index).musics.get(0).filePath;
                setSong(userController.imageView,currPath);
            }
        }
    }

    private void setSong(ImageView myImage,String currPath) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File curr_mp3 = new Mp3File(currPath);
        if (curr_mp3.hasId3v2Tag()) {
            ID3v2 id3v2Tag = curr_mp3.getId3v2Tag();
            Image Image = new Image(new ByteArrayInputStream(id3v2Tag.getAlbumImage()));
            myImage.setImage(Image);
        }
        songLabel.setText(Queue.list.get(Queue.songNumber).artist + " - " + Queue.list.get(Queue.songNumber).name);
        songProg.setProgress(0);
    }
}