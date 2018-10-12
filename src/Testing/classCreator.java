package Testing;

import java.io.IOException;

public class classCreator {
    public static void main(String[] args) throws IOException, InterruptedException{
        Runtime r = Runtime.getRuntime();
        //String fileName = "src/Testing/testJar";
        Process p = r.exec("javac -cp java-json.jar;src src/Distributed/Example.java");
        p.waitFor();
      //  Process e = r.exec("java -cp src Testing.testJar");
        //e.waitFor();
    }
}
