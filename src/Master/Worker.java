package Master;

import GsonInformation.Gsons;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @class Worker
 * For every connected worker, the master have an instance of this class.
 * A basic buffer of the communication with the worker
 */

public class Worker{
    private Socket socket;
    private PrintStream printSteam;
    private Scanner scan;
    private int id;
    private boolean active;
    private boolean working;
    private String answer;
    private String name;

    public Worker(int id, Socket socket) throws IOException {
        working = true;
        active = false;
        this.id = id;
        this.name = "Worker"+id;
        this.socket = socket;
        printSteam = new PrintStream(socket.getOutputStream());
        scan = new Scanner(socket.getInputStream());
    }

    /**
     * send message to the worker
     * @param packet the url of the jar file
     */
    public void send(Gsons.Packet packet) throws IOException{
        active = true;
        name = packet.name;
       // System.out.println("well wtf i am sending?!?!?!?!");
        printSteam.println(Gsons.gson.toJson(packet));
        printSteam.flush();
      //  System.out.println(Gsons.gson.toJson(packet));
    }

    /**
     * @return the answer of the worker to the master
     */
    public String getInput() throws IOException{
        if(scan.hasNextLine()){
            String str = scan.nextLine();
            if(str == "kill"){
                close(CloseType.Kill);
            }
            if(checkAnswer(str)){
                active = false;
                return str;
            }
        }
        return null;
}

    /**
     * @param input from the worker
     * @return if the input contains the answer
     */
    private boolean checkAnswer(String input){
        return input != "kill";
    }

    /**
     * @return if the worker is active - if he is he may be dead
     */
    public boolean isActive(){
        return active;
    }

    /**
     * @return if the worker is alive
     */
    public boolean isWorking(){
        return working;
    }

    /**
     * this function close the worker and tells the worker to close itself
     * @throws IOException closing socket
     */
    public void close(CloseType type) throws  IOException{
        String[] a = {""};
        send(new Gsons.Packet(false, "", "", type.toString(), a, a, a));
        working = false;
        active = false;
        scan.close();
        printSteam.close();
    }

}
