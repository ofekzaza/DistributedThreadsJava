package Testing;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class wow {
    public static void print(String a) throws IOException{
        FileOutputStream file = new FileOutputStream("file.txt");
        PrintStream ps = new PrintStream(new DataOutputStream(file));
        ps.print(a);
    }
}
