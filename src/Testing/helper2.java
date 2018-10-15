package Testing;

import GsonInformation.Gsons;
import Master.IpBroadcast;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class helper2 {
    public static void main(String[] args)throws Exception{
        ServerSocket serverSocket = new ServerSocket(1948);
        IpBroadcast broadcast = new IpBroadcast();
        broadcast.start();
        print("sended ip");
        Socket socket = serverSocket.accept();
        print("got communication");
        print("started commpiling");
       // JSONObject jsonObject = new JSONObject();
        //jsonObject.put("Working", true);
        //jsonObject.put("Name", "Example");
        JSONObject object = new JSONObject();
        object.put("Ints", new JSONArray());
        object.put("Booleans", new JSONArray());
        object.put("Doubles", new JSONArray());
        object.put("Strings", new JSONArray());
        //jsonObject.put("Input", object);
        print(" finish writing input");
        print("have compilation");
        Scanner input = new Scanner(new FileInputStream("src/Distributed/Example.java"));
       // BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

        String str = "";

        while(input.hasNextLine()){
            str += input.nextLine();
        }

        Gsons.Packet packet = new Gsons.Packet(true, str, "Example", object.toString(), "");

        str = Gsons.gson.toJson(packet);

        //jsonObject.put("ClassFile", str);

        print("got output" + str);

        Scanner scanner = new Scanner(socket.getInputStream());
        PrintStream out = new PrintStream(socket.getOutputStream());
        //jsonObject.put("ClassFile", str);
        out.println(str);
        out.flush();

        print("sended output which is");

        //out.println(str);
        //out.flush();

        while(!scanner.hasNext()){}
        System.out.println("answer is:"+scanner.next());
        packet.alive = false;
        str = Gsons.gson.toJson(packet);
        out.println(str);
        out.flush();
        out.close();
    }
    public static void print(String str){
        System.out.println(str);
    }
}
