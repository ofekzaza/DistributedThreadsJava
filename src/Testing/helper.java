package Testing;

import Distributed.Example;
import Master.MasterSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class helper {
    public static void main(String[] args) throws JSONException, IOException{
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("GOOD");
        json.put(MasterSocket.JsonNames[3], array);
        json.put(MasterSocket.JsonNames[0], new JSONArray());
        json.put(MasterSocket.JsonNames[1], new JSONArray());
        json.put(MasterSocket.JsonNames[2], new JSONArray());
        FileOutputStream file = new FileOutputStream("src/Worker/input.txt");
        System.out.println(json.toString());
        file.write(json.toString().getBytes());
        file.close();
        Example.main(args);
    }
}
