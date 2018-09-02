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
            sockets.add(serverSocket.accept());
            System.out.println("got connection");
            startCommunication();

            while (working) {
                sockets.add(serverSocket.accept());
                workers.add(new Worker(sockets.size()-1, sockets.get(sockets.size()-1)));
            }

            //TODO, close workers
            for(int i = 0;i < workers.size(); i++)
                workers.get(i).kill();

            serverSocket.close();
        }catch (IOException x){
            x.printStackTrace();
        }
        System.out.println("master socket is dead");
    }

    public void waitForResults() {
        try {
            Thread.sleep(5000);
        }catch (Exception x){}
    }

    /**
     * ends the while loop
     */
    public void kill(){
        working = false;
        try{
            for(int i = 0;i < sockets.size(); i++)
                workers.get(i).kill();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }
}
