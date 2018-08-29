package Master;

import Worker.WorkerMain;

import java.net.Inet4Address;

public class MasterDThread <T extends MasterDThread>{
    private IpBroadcast broadcast;
    private int broadcastPort = 1861;
    private T child;

    public static void main(String[] args) throws  Exception{
        WorkerMain.helper();
    }

    /**
     * main of the library
     */
    public void start(){
        startBroadcast();

        child.run();

        endRun();
    }

    //function for the child
    public void run(){

    }

    /**
     * this functions start the ip broadcast
     * broadcastPort the port of the broadcast
     */
    private void startBroadcast(){
        broadcast = new IpBroadcast(broadcastPort);
        broadcast.start();
    }

    /**
     * clean the program after the end, like finalized
     */
    private void endRun(){
        broadcast.kill();
    }
}
