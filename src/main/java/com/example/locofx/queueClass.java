package com.example.locofx;

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

    private void setMedia() {
        System.out.println("11");
        currFile = new File(list.get(songNumber).filePath);
        System.out.println(list.get(songNumber).filePath);
        System.out.println(currFile.toURI().toString());
        System.out.println("12");
        media = new Media(currFile.toURI().toString());
        System.out.println("13");
        mediaPlayer = new MediaPlayer(media);
        System.out.println("14");
    }

}


