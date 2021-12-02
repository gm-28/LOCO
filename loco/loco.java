package loco;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

public class loco {

    public static void main(String[] args) throws InvalidDataException, UnsupportedTagException, IOException {
        Library lib = new Library();
        //lib.read_file("C:\\Users\\Nelio David\\Desktop\\materiais_proj\\DJ LILOCOX - Paz & Amor\\Samba.mp3");
        lib.getLibrary("C:\\Users\\Nelio David\\Desktop\\materiais_proj\\DJ LILOCOX - Paz & Amor");
    }
}
