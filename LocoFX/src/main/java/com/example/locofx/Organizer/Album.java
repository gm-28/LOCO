package com.example.locofx.Organizer;

import java.util.ArrayList;

public class Album {
    public String title;
    public ArrayList<Music> musics;

    public Album(String title){
        this.title = title;
        musics = new ArrayList<Music>();
    }
}

