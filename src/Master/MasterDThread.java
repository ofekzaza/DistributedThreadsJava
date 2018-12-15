package Master;

import java.io.IOException;

public class MasterDThread <T extends MasterDThread>{
    private IpBroadcast broadcast;
    private final int broadcastPort = 1861; // dont change
    private T child;
    private String[] fileList;
    private MasterSocket masterSocket;
    private CloseType closeType;

    public static void main(String[] args) throws  Exception{
    }

    @Deprecated
    public MasterDThread(){

    }

    /**
     * constructor
     * @param child extends self, the class the calls the library
     * @param closeType the type of the deconstruction of the program
     * @param files the name of the files from Distributed Package you wish to work with.
     */
    public MasterDThread(T child, CloseType closeType, String ... files){
        this.child = child;
        this.closeType = closeType;
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
     * close the program and the sockets
     * @param type what type of closing we would have
     *             Kill is a total close
     *             PreFor... closing the master but tells the worker to stay and work until a new master attract the workers
     */
    public void close(CloseType type)
    {
        broadcast.kill();
        masterSocket.close(type);
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
     * @param name the name of the execution
     * @param location the java code file including the package
     * @param dependencies the dependencies of the code include the packages (only java files) (Package/File)
     * @param sources the sources of the dependencies (source dir for example src)
     * @param params the input of the code
     */
    public void execute(String name, String location, String[] sources, String[] dependencies, String... params){
        masterSocket.giveMission(name, location, sources, dependencies, params);
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
     * The main of the library
     */
    public void start(){
        startBroadcast();
        startTcpWorkerConnection();
        child.run();
        close(closeType);
    }
}
