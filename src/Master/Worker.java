package Master;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Worker{
    private Socket socket;
    private OutputStream outputStream;
    private PrintStream printSteam;
    private InputStream inputStream;
    private Scanner scan;
    private String name;
    private int id;
    private String message;
    private boolean active;
    private boolean working;
    private String answer;

    public Worker(int id, Socket socket) throws IOException {
        working = true;
        active = false;
        this.id = id;
        this.name = "Worker"+id;
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        printSteam = new PrintStream(outputStream);
        scan = new Scanner(inputStream);
    }

    /**
     * send message to the worker
     * @param str the url of the jar file
     */
    public void send(String str){
        //TODO
    }

    /**
     *
     * @return the answer of the worker to the master
     */
    public String getInput() throws IOException{
        if(scan.hasNext()){
            String str = scan.next();
            if(str == "kill"){
                kill();
                answer = null;
            }
            if(checkAnswer(str)){
                answer = str.substring(6);
            }
        }
        if(answer == null)
            return "";
        return answer;
    }

    /**
     * @param input from the worker
     * @return if the input contains the answer
     */
    private boolean checkAnswer(String input){
        String help = input.substring(0,6);
        return help.toLowerCase() == "answer";
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
     * this function close the worker
     * @throws IOException closing socket
     */
    public void kill() throws  IOException{
        working = false;
        active = true;
        scan.close();
        inputStream.close();
        printSteam.close();
        outputStream.close();
        socket.close();
    }

}
