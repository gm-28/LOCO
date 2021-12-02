package loco;

import java.util.ArrayList;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.*;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

public class Album {
    public String Title;
    public ArrayList<String> songs;

    public Album(String title){
        Title = title;
    }

    public void songAdd(String song_path) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File curr = new Mp3File(song_path);
        if (curr.hasId3v1Tag()) {
            ID3v1 id3v1Tag = curr.getId3v1Tag(); //verifica se tem a tag e insere a faixa na lista consoante o track number
            int i = Integer.parseInt(id3v1Tag.getTrack());
            songs.add(i, song_path);
        }
    }
}

