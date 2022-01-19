package com.example.locofx.Organizer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

public class Music {
    public String filePath;
    public String name;
    public String artist;

    public Music(String filePath, String Artist){//para varios alterar a declaração do constructor
        this.filePath = filePath;
        this.artist= Artist;
    }

    public void fetch_metadata() throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File curr_mp3 = new Mp3File(filePath);
    }
}
