package Testing;

import java.io.IOException;

public class classCreator {
    public static void main(String[] args) throws IOException, InterruptedException{
        Runtime r = Runtime.getRuntime();
        String fileName = "src/Testing/letsSee";
        Process p = r.exec("javac "+fileName+".java");
        p.waitFor();
        Process e = r.exec("java -cp src Testing.letsSee");
        e.waitFor();
    }
}
