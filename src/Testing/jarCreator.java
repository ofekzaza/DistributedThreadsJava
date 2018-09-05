package Testing;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class jarCreator {
    public static void main(String[] args) throws IOException {
        //prepare Manifest file
        String version = "1.0.0";
        String author = "me";
        String main = "src/Testing/test.java";
        Manifest manifest = new Manifest();
        Attributes global = manifest.getMainAttributes();
        global.put(Attributes.Name.MANIFEST_VERSION, version);
        global.put(new Attributes.Name("Created-By"), author);
        global.put(Attributes.Name.MAIN_CLASS, main);

        //create required jar name
        String jarFileName ="testJar.jar";
        JarOutputStream jos = null;
            File jarFile = new File(jarFileName);
            jarFile.delete();
            jarFile = new File(jarFileName);
            OutputStream os = new FileOutputStream(jarFile);
            jos = new JarOutputStream(os, manifest);

        //Collect all file and class names to iterate
        List<String> fileList = new ArrayList<String>();
        String rootLocation ="";
        fileList.add("src/Testing/testJar.java");
        //fileList.add("javax/swing/BoxBeanInfo.class");
      //  fileList.add("javax/swing/text/JTextComponentBeanInfo.class");

        //start writing in jar
        int len =0;
        byte[] buffer = new byte[1024];
        for(String file : fileList ){
            //create JarEntry
            JarEntry je = new JarEntry("src/Testing/testJar.class");
            je.setComment("Creating Jar");
            je.setTime(Calendar.getInstance().getTimeInMillis());
            System.out.println(je);
            jos.putNextEntry(je);

            //write the bytes of file into jar
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            while((len = is.read(buffer, 0, buffer.length)) != -1) {
                jos.write(buffer, 0, len);
            }
            is.close();
            jos.closeEntry();

            Scanner scan = new Scanner(new DataInputStream(new FileInputStream(file)));

            while(scan.hasNext()){
                System.out.println(scan.next());
            }
            scan.close();
        }

        jos.close();
        System.out.println("Done");
    }
}