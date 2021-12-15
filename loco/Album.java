package loco;

import java.util.ArrayList;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.*;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

public class Album {
    public String title;
    public ArrayList<Music> musics;

    public Album(String title){
        this.title = title;
        musics = new ArrayList<Music>();
    }

    public void musicsAdd(Music music){
        musics.add(music);
    }
}

