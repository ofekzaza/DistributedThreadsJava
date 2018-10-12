package Master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitForWorkers extends Thread{
    private Thread thread;
    private String name;
    private ServerSocket serverSocket;
    private boolean working;

    /**
     * @param serverSocket MasterSocket ServerSocket
     * @param port the port which ServerSocket is listening to
     */
    public WaitForWorkers(ServerSocket serverSocket, int port){
        name = "WaitForWorkersThread"+port;
        this.serverSocket = serverSocket;
        working = true;
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
    }

    public void close(){
        working = false;
    }

    @Override
    public void start(){
        thread = new Thread(this, name);
        thread.start();
    }
}
