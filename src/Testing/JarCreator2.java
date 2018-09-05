package Testing;

import java.io.*;
import java.util.Scanner;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarCreator2 {
    public static void main(String[] args) throws IOException{
        String fileName = "testJar.jar";
        FileOutputStream file = new FileOutputStream("testJar.jar");
        JarOutputStream jarOut = new JarOutputStream(file);
        //jarOut.putNextEntry(new ZipEntry("src/")); // Folders must end with "/".
        jarOut.putNextEntry(new ZipEntry("testJar.class"));

        byte[] buffer = new byte[1024];
        int len = 0;

        BufferedInputStream is = new BufferedInputStream(new FileInputStream("src/Testing/testJar.java"));

        while((len = is.read(buffer, 0, buffer.length)) != -1) {
            System.out.println(len);
            jarOut.write(buffer, 0, len);
        }

        Scanner scan = new Scanner(new DataInputStream(new FileInputStream("src/Testing/testJar.java")));

        String input;
        while(scan.hasNext()) {
            input = scan.next();
            System.out.println(input);
           // jarOut.write(input.getBytes());
           // System.out.println(input.);
        }
        jarOut.closeEntry();
        jarOut.close();
        jarOut.close();
        file.close();
    }
}
