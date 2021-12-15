package Loco;

import java.util.ArrayList;
import java.util.Objects;

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

    public int album_exists(String title){
        for(int i=0;i<albums.size();i++){
            if (Objects.equals(albums.get(i).title, title))
                return i;
        }
        return -1;
    }

}
