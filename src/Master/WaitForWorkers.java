package Master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitForWorkers extends Thread{
    private Thread thread;
    private String name;
    private ServerSocket serverSocket;
    private boolean working;
    private boolean died;

    /**
     * @param serverSocket MasterSocket ServerSocket
     * @param port the port which ServerSocket is listening to
     */
    public WaitForWorkers(ServerSocket serverSocket, int port){
        name = "WaitForWorkersThread"+port;
        this.serverSocket = serverSocket;
        working = true;
        died = false;
    }

    /**
     * this thread waits to worker to join the server socket so the program is flexible.
     */
    @Override
    public void run(){
        while(working) {
            try {
                MasterSocket.init().addWorker(serverSocket.accept());
            }catch (IOException x){
                x.printStackTrace();
            }
        }
        died = true;
        System.out.println("wait for workers is dead!!!!!");
    }


    /**
     * closing the thread
     */
    public void close(){
        working = false;
    }

    /**
     *
     * @return if the thread have died
     */
    public boolean isDead(){
        return died;
    }

    @Override
    public void start(){
        thread = new Thread(this, name);
        thread.start();
    }
}
