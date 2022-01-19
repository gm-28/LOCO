package com.example.locofx.Controllers;

import com.example.locofx.Organizer.Music;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class queueClass {
    public ArrayList<Music> list;
    public Media media;
    public MediaPlayer mediaPlayer;
    public File currFile;
    int songNumber;

    queueClass(ArrayList<Music> init)
    {
        list = init;
        songNumber = 0;
        setMedia();
    }

    public void resetQ() {
        songNumber = 0;
        setMedia();
    }

    public void setNext() {
        if(list.size()-1 > songNumber){
            songNumber++;
        }else{
            songNumber=0;
        }
        mediaPlayer.stop();
        setMedia();
    }

    public void setPrevious() {
        if(songNumber > 0){
            songNumber--;
        }else{
            songNumber= list.size()-1;
        }
        mediaPlayer.stop();
        setMedia();
    }

    private void setMedia(){
        currFile = new File(list.get(songNumber).filePath);
        media = new Media(currFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }
}


