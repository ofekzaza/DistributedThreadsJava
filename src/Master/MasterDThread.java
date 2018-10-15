package Master;

import Worker.WorkerMain;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;

public class MasterDThread <T extends MasterDThread>{
    private IpBroadcast broadcast;
    private final int broadcastPort = 1861; // dont change
    private T child;
    private String[] fileList;
    private MasterSocket masterSocket;

    public static void main(String[] args) throws  Exception{
    }

    @Deprecated
    public MasterDThread(){

    }

    /**
     * constructor
     * @param child extends self, the class the calls the library
     * @param files the name of the files from Distributed Packege you wish to work with.
     //* @param name the name of the current master
     */
    public MasterDThread(T child, String ... files){
        this.child = child;
        this.fileList = files;
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
        broadcast.waitForBroadcast();
    }

    /**
     * clean the program after the end, like finalized
     */
    private void endRun()
    {
        System.out.println("the dthread is closing");
        broadcast.kill();
        masterSocket.close();
    }

    /**
     * this functions is responsible for waiting for worker connection
     */
    private void startTcpWorkerConnection(){
        masterSocket = masterSocket.init();
        masterSocket.start();
        masterSocket.waitForConnection();
    }

    /**
     * block the thread until all workers return an answer
     */
    public void waitForResults(){
        masterSocket.waitForResults();
    }

    /**
     * make a worker executes the java file name
     * @param name the java code file
     * @param dependencies the dependencies of the code, dont use
     * @param params the input of the code
     */
    public void execute(String name, String dependencies, String... params){
        masterSocket.giveMission(name, dependencies, params);
    }

    /**
     *
     * @param name the name of the code java file
     * @return the string result or "" if there is no result or there is no result
     * @throws IOException
     */
    public String getAnswer(String name){
        try {
            return masterSocket.getAnswer(name);
        }catch (IOException x){
            x.printStackTrace();
        }
        return "";
    }

    /**
     * main of the library
     */
    public void start(){
        startBroadcast();
        System.out.println("start tcp");
        startTcpWorkerConnection();
        System.out.println("start child main");
        child.run();

       // masterSocket.waitForResults();
        System.out.println("start closing");
        endRun();
    }
}
