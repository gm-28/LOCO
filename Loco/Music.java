package Loco;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

public class Music {
    public String filePath;
    public String name;

    public Music(String filePath){//para varios alterar a declaração do constructor
        this.filePath = filePath;
    }

    public void fetch_metadata() throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File curr_mp3 = new Mp3File(filePath);
    }
}
