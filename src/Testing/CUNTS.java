package Testing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ofeke on 12/11/2018.
 */
public class CUNTS {
    public static void main(String[] args) throws IOException{
        FileOutputStream outputStream = new FileOutputStream("FUCLK.txt");
        outputStream.write(5555555);
        FileInputStream input = new FileInputStream("FUCLK.txt");
        System.out.println(input.read());
    }
}
