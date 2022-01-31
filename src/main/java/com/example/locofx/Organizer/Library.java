package com.example.locofx.Organizer;

import com.mpatric.mp3agic.*;
import com.mpatric.mp3agic.Mp3File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Library {
    public ArrayList<Music> musics = new ArrayList<>();
    public ArrayList<Album> albums = new ArrayList<>();
    public ArrayList<Artist> artists = new ArrayList<>();

    public boolean getLibrary(String A_path) throws InvalidDataException, UnsupportedTagException, IOException {
        List<Path> filesList;
        Path path = Paths.get(A_path);
        if(Files.exists(path)){
            filesList = Files.walk((Paths.get(A_path)))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".mp3"))
                    .collect(Collectors.toList());

            for (Path file : filesList) {
                Mp3File curr_mp3 = new Mp3File(file.toAbsolutePath().toString());
                Music curr_music = new Music(file.toAbsolutePath().toString(), curr_mp3.getId3v2Tag().getArtist());

                if (!musics.contains(curr_music)) {
                    musics.add(curr_music);
                    if (curr_mp3.hasId3v2Tag()) {

                        ID3v2 id3v2Tag = curr_mp3.getId3v2Tag();

                        Album curr_alb = new Album(id3v2Tag.getAlbum());
                        Artist curr_artist = new Artist(id3v2Tag.getArtist());
                        musics.get(musics.size() - 1).name = id3v2Tag.getTitle();
                        curr_alb.musics.add(musics.get(musics.size() - 1));

                        createLibrary(curr_music, curr_alb, curr_artist);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void createLibrary(Music curr_music, Album curr_alb, Artist curr_artist){
        int index = artist_exists(curr_artist.name);
        if (index<0){
            artists.add(curr_artist);
            int index2 = artists.get(artists.size()-1).album_exists(curr_alb.title);
            if (index2<0){
                albums.add(curr_alb);
                artists.get(artists.size()-1).albums.add(curr_alb);
            }
            else{
                artists.get(artists.size()-1).albums.get(index2).musics.add(curr_music);
            }
        }
        else{
            int index2 = artists.get(artists.size()-1).album_exists(curr_alb.title);
            if (index2<0){
                albums.add(curr_alb);
                artists.get(index).albums.add(curr_alb);
            }
            else{
                artists.get(index).albums.get(index2).musics.add(curr_music);
            }
        }
    }

    public int artist_exists(String name){
        for(int i=0;i<artists.size();i++){
            if (Objects.equals(artists.get(i).name, name))
                return i;
        }
        return -1;
    }

    public int lookUpSong(String name){
        for(int i=0;i<musics.size();i++){
            if (Objects.equals(musics.get(i).name, name))
                return i;
        }
        return -1;
    }

    public int lookUpAlbum(String name){
        for(int i=0;i<albums.size();i++){
            if (Objects.equals(albums.get(i).title, name))
                return i;
        }
        return -1;
    }
}