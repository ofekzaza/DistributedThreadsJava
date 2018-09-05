package Testing;

import sun.tools.jar.resources.jar;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class readJar {
    public static void main(String[] args) throws Exception{
        /**
         * Scanner scan = new Scanner(new DataInputStream(new FileInputStream("testJar.jar")));
         while(scan.hasNext()){
         System.out.println(scan.next());
         }
         */
        JarInputStream jis = new JarInputStream(new FileInputStream("testJar.jar"));

        Manifest man = jis.getManifest();

        //FileInputStream inputStream = new FileInputStream("testJar.jar");
       // man.read(inputStream);

       Scanner scan = new Scanner(jis);

        while(scan.hasNext()){
            System.out.println(scan.next());
        }
    }
}
