package Master;

import Worker.WorkerMain;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;

public class MasterDThread <T extends MasterDThread>{
    private IpBroadcast broadcast;
    private final int broadcastPort = 1861; // dont change
    private String name;
    private T child;
    private MasterSocket masterSocket;

    public static void main(String[] args) throws  Exception{
    }

    /**
     * constructor
     * @param child extends self, the class the calls the library
     * @param name the name of the current master
     */
    public MasterDThread(T child, String name){
        this.child = child;
        this.name = name;
    }

    //function for the child
    protected void run(){

    }

    /**
     * this functions start the ip broadcast
     * broadcastPort the port of the broadcast - dont change broadcast port, if you do you need to change the port in every worker
     */
    private void startBroadcast(){
        broadcast = new IpBroadcast(broadcastPort);
        broadcast.start();
    }

    /**
     * clean the program after the end, like finalized
     */
    private void endRun()
    {
        broadcast.kill();
        masterSocket.kill();
    }

    /**
     * this functions is responsible for waiting for worker connection
     */
    private void startTcpWorkerConnection(){
        masterSocket = masterSocket.init();
        masterSocket.start();

    }

    /**
     * main of the library
     */
    public void start(){
        startBroadcast();
        startTcpWorkerConnection();

        child.run();

        endRun();
    }
}
