package Distributed;

public class helper {
    public static void main(String[] args) throws Exception{
        Runtime r = Runtime.getRuntime();
        String fileName = "src/Distributed/DistributedFunction";
      //  Process p = r.exec("javac "+fileName+".java");
        //p.waitFor();
        fileName = "src/Distributed/DistributedFunction";
        Process d = r.exec("javac "+fileName+".java");
        fileName = "src/Distributed/Example";
        d.waitFor();
        Process e = r.exec("javac "+fileName+".java");
        e.waitFor();
       // Process e = r.exec("java -cp src Distributed.Example");
      //  e.waitFor();
    }
}
