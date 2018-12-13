package Distributed;

import GsonInformation.Gsons;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class DistributedFunction {
    private String fileInputName;
    private FileInputStream inputStream;
    private DistributedFunction child;
    private Scanner scanner;
    private Gsons.Basic inputGson;

    /**
     * default constructor for child
     */
    @Deprecated
    public DistributedFunction(){

    }

    /**
     * the constructor of the worker file class
     * @param child the child script
     * @throws IOException reads the input from a file
     */
    public DistributedFunction(DistributedFunction child){
        try {
            fileInputName = "src/Worker/input.txt";
            this.child = child;
            inputStream = new FileInputStream(fileInputName);
            scanner = new Scanner(inputStream);
            String input = "";
            while (scanner.hasNextLine()) {
                input += scanner.nextLine();
            }
            inputGson = Gsons.JSON_CACHE_PARSER_BASIC.fromJson(input);
            scanner.close();
            inputStream.close();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    /**
     * a main is needed in the son
     * @param args defualt values get from the compiler
     */

    /**
     * the main of the class file
     * gives the child a
     */
    public void start(){
        try {
            FileOutputStream outputStream = new FileOutputStream("src/Worker/answer.txt");
            outputStream.write(child.run(inputGson.strings).getBytes());
            outputStream.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }

    /**
     * the function which the user writes
     * @param args string inputs
     * @return String - string can be anything if its managed the right way.
     */
    public String run(String[] args){return "";};
}
