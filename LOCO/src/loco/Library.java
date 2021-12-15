package loco;

import com.mpatric.mp3agic.*;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
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

    public void read_file(String path) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File song = new Mp3File(path);
        System.out.println("Length of this mp3 is: " + song.getLengthInSeconds() + " seconds");
    }

    public void getLibrary(String A_path) throws InvalidDataException, UnsupportedTagException, IOException {
        List<Path> filesList;
        filesList = Files.walk((Paths.get(A_path)))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".mp3"))
                .collect(Collectors.toList());

        for (Path file : filesList) {
                Music curr_music = new Music(file.toAbsolutePath().toString());
                Mp3File curr_mp3 = new Mp3File(curr_music.filePath);

                if (!musics.contains(curr_music)){
                    musics.add(curr_music);
                    if (curr_mp3.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = curr_mp3.getId3v2Tag(); //verifica se tem a tag e insere a faixa na lista consoante o track number
                        Album curr_alb = new Album(id3v2Tag.getAlbum());
                        Artist curr_artist = new Artist(id3v2Tag.getArtist());

                        int index = artist_exists(curr_artist.name);
                        if (index<0){
                            artists.add(curr_artist);
                            int index2 = album_exists(curr_alb.title);
                            if (index2<0){
                                albums.add(curr_alb);
                                albums.get(albums.size()-1).musicsAdd(curr_music);
                            }
                            else{
                                albums.get(index2).musicsAdd(curr_music);
                            }

                        }
                        else{
                            artists.get(index).albumsAdd(curr_alb);

                        }
                    }
                }
            }


        print_library();
    }

    //Tf the album exists returns its index, if not returns -1
    private int album_exists(String title){
        for(int i=0;i<albums.size();i++){
            if (Objects.equals(albums.get(i).title, title))
                return i;
        }
        return -1;
    }

    private int artist_exists(String name){
        for(int i=0;i<artists.size();i++){
            if (Objects.equals(artists.get(i).name, name))
                return i;
        }
        return -1;
    }

    private  void print_library(){
        for(int d=0;d<musics.size();d++){
            System.out.println(musics.get(d).filePath);
        }
        System.out.println("Number of albums:"+albums.size());
        for(int k=0;k<albums.size();k++){
            Album a = albums.get(k);
            System.out.println("Album Tittle:"+a.title);
            System.out.println("Album size:"+a.musics.size());
            for(int j=0;j<a.musics.size();j++){
                System.out.println("Music path"+a.musics.get(j).filePath);
            }
        }
        for(int a=0;a<artists.size();a++){
            System.out.println(artists.get(a).name);
            for(int k=0;k<artists.get(a).albums.size();k++){
                Album asd = artists.get(a).albums.get(k);
                System.out.println("Album Tittle:"+asd.title);
                System.out.println("Album size:"+asd.musics.size());
                for(int j=0;j<asd.musics.size();j++){
                    System.out.println("Music path"+asd.musics.get(j).filePath);
                }
            }
        }
    }
}


