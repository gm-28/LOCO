package com.example.locofx;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
        System.out.println("10");
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

    /*public void addToQueue(ArrayList<Music> musicAdd) {
        list.addAll(musicAdd);
    }*/

    public void addToQueue2(Music musicAdd) {
        list.add(musicAdd);
    }

    private void setMedia(){
        currFile = new File(list.get(songNumber).filePath);
        System.out.println(list.get(songNumber).filePath);
        System.out.println(currFile.toURI().toString());
        media = new Media(currFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }
}


