package Distributed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class DistributedFunction {
    private String fileInputName;
    private FileInputStream inputStream;
    private DistributedFunction child;
    private JSONObject jsonObject;
    private DataInputStream dataInputStream;
    private Scanner scanner;
    private int[] ints;
    private String[] strings;
    private double[] doubles;
    private boolean[] booleans;

    /**
     * default constructor for child
     */
    public DistributedFunction(){

    }

    /**
     * the constructor of the worker file class
     * @param child the child script
     * @throws IOException reads the input from a file
     * @throws JSONException the input is in json format
     */
    public DistributedFunction(DistributedFunction child){
        try {
            fileInputName = "src/Worker/input.txt";
            this.child = child;
            inputStream = new FileInputStream(fileInputName);
            dataInputStream = new DataInputStream(inputStream);
            scanner = new Scanner(dataInputStream);
            String input = "";
            while (scanner.hasNext()) {
                input += scanner.next();
            }
            jsonObject = new JSONObject(input);
            JSONArray array = jsonObject.getJSONArray("Ints");
            ints = new int[array.length()];
            for (int i = 0; i < array.length(); i++) {
                ints[i] = array.getInt(i);
            }
            array = jsonObject.getJSONArray("Doubles");
            doubles = new double[array.length()];
            for (int i = 0; i < array.length(); i++) {
                doubles[i] = array.getDouble(i);
            }
            array = jsonObject.getJSONArray("Booleans");
            booleans = new boolean[array.length()];
            for (int i = 0; i < array.length(); i++) {
                booleans[i] = array.getBoolean(i);
            }
            array = jsonObject.getJSONArray("Strings");
            strings = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                strings[i] = array.getString(i);
            }
            scanner.close();
            dataInputStream.close();
            inputStream.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }

    /**
     * a main is needed in the son
     * @param args defualt values get from the compiler
     */

    /**
     * the main of the class file
     */
    public void start(){
        try {
            FileOutputStream outputStream = new FileOutputStream("src/Worker/answer.txt");
            outputStream.write(child.execute(ints, doubles, booleans, strings).getBytes());
            outputStream.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }

    /**
     * the function which the user writes
     * @param ints int inputs
     * @param doubles double inputs
     * @param booleans boolean inputs
     * @param strings string inputs
     * @return String - string can be anything if its managed the right way.
     */
    public String execute(int[] ints, double[] doubles, boolean[] booleans, String[] strings){
        return "";
    }
}
