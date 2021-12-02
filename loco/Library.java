package loco;

import com.mpatric.mp3agic.*;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Library {
    public ArrayList<String> songs = new ArrayList<>();

    public void read_file(String path) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File song = new Mp3File(path);
        System.out.println("Length of this mp3 is: " + song.getLengthInSeconds() + " seconds");
    }

    public void getLibrary(String A_path) throws InvalidDataException, UnsupportedTagException, IOException {
        File path = new File(A_path);
        File[] filesList = path.listFiles();
        int i =0;
        for (File file : filesList) {
            //Mp3File add = new Mp3File("/src/DJ LILOCOX - PAZ & AMOR/"+file.getName());

            if(file.getName().endsWith(".mp3")) {
                songs.add(file.getAbsolutePath());
                System.out.println("File path: " + songs.get(i));
                System.out.println(" ");
                i++;
            }

            /*System.out.println("File name: " + file.getName());
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("Size :" + file.getTotalSpace());
            System.out.println("Length of this mp3 is: " + songs.get(i).getLengthInSeconds() + " seconds");
            System.out.println(" ");
            i++;*/
        }
        Mp3File teste = new Mp3File(songs.get(0));
        System.out.println("Length of this mp3 is: " + teste.getLengthInSeconds() + " seconds");


    }
}
