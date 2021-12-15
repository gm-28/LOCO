package loco;

import java.util.ArrayList;

public class Artist {
    public String name;
    public ArrayList<Album> albums;

    public Artist(String name){
        this.name=name;
        albums = new ArrayList<>();
    }

    public void albumsAdd(Album alb) {
        albums.add(new Album(alb.title));
    }
}
