package Master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * singalton responsible from communication with the workers
 */
public final class MasterSocket extends Thread{
    private Thread thread;
    private String name = "MasterSocket";
    private final static MasterSocket instance = new MasterSocket();
    private ServerSocket serverSocket;
    private final int port = 1948;
    private boolean working;
    public ArrayList<Socket> sockets;
    private ArrayList<Worker> workers;

    private MasterSocket(){
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException x){
            x.printStackTrace();
        }
        working = true;
        sockets = new ArrayList<Socket>();
        workers = new ArrayList<Worker>();
        System.out.println("Good at least");
    }

   // public static void main(String[] args){
     //   MasterSocket socket = MasterSocket.init();
       // socket.run();
     //   while(socket.sockets.size() == 0){}
       // socket.kill();
    //}

    public static MasterSocket init(){
        return instance;
    }

    public void startCommunication(){
        //TODO
    }

    /**
     * function main
     */
    @Override
    public void run() {
        try {
            while (working) {
                sockets.add(serverSocket.accept());
                System.out.println("got connection");
                if(sockets.size() == 1){
                    startCommunication();
                }
            }
            //TODO, close workers
            for(int i = 0;i < sockets.size(); i++)
                sockets.get(i).close();
            serverSocket.close();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    /**
     * ends the while loop
     */
    public void kill(){
        working = false;
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }
}
