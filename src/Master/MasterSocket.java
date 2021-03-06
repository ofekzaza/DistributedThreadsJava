package Master;


import GsonInformation.Gsons;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * singalton responsible from communication with the workers
 */
public final class MasterSocket extends Thread {

    /**
     * @param thread the singleton is a thread
     * @param name the name of the thread
     * @param MasterSocket self
     * @param serverSocket the server socket of the master, using tcp to communicate with the workers
     * @param port the port of the program
     * @param working says if the masterSocket should work
     * @param sockets storage of the sockets which connects the workers
     * @param workers an arraylist of the workers supervisors class
     * @param locationQ a stack of the file locations of the Distributed code files
     * @param inputQ the input for the code file
     * @param dependenciesQ the dep of the code file
     * @param sourcesQ the sources of the dependencies
     * @param recivingMaster a thread which is waiting for workers to connect to the master
     * @param scanner a scanner for reading files // not neccery you can change it in order to make the program run faster a little
     * @param connection if have a connection with at least one workers
     * @param lastBusy number which say which workers is the last one who got a job
     * @param workerMap a map of the workers with the name of the algoritem which run on them
     * @param fileReader a FileInputStream for reading files
     * @param closeType the closing type of the workers
     * @param dependenciesFiles something which related to the dependencies of the worker
     */

    private Thread thread;
    private String name = "MasterSocket";
    private final static MasterSocket instance = new MasterSocket();
    private ServerSocket serverSocket;
    private final int port = 9001; // dont change
    private boolean working;
    public ArrayList<Socket> sockets;
    private ArrayList<Worker> workers;
    private LinkedList<String> locationQ;
    private LinkedList<String[]> inputQ;
    private LinkedList<String[]> dependenciesQ;
    private LinkedList<String[]> sourcesQ;
    private LinkedList<String> nameQ;
    private WaitForWorkers recivingMaster;
    private Scanner scanner;
    private boolean connection;
    private int lastBusy;
    private HashMap<String, Worker> workersMap;
    private FileInputStream fileReader;
    private CloseType closeType;
    private String[] dependenciesFiles;

    private MasterSocket() {
        connection = false;
        closeType = CloseType.Kill;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException x) {
            x.printStackTrace();
        }
        lastBusy = 0;
        working = true;
        sockets = new ArrayList<Socket>();
        workers = new ArrayList<Worker>();
        nameQ = new LinkedList<>();
        locationQ = new LinkedList<>();
        inputQ = new LinkedList<>();
        sourcesQ = new LinkedList<>();
        workersMap = new HashMap<>();
        dependenciesQ = new LinkedList<>();
        recivingMaster = new WaitForWorkers(serverSocket, port);
    }

    public static MasterSocket init() {
        return instance;
    }

    /**
     * function main
     */
    @Override
    public void run() {
        try {
            //wait to get the first connection
            //System.out.println("waiting for the worker");
            sockets.add(serverSocket.accept());
            //System.out.println("got a bloody worker");
            //System.out.println(sockets.size());
            workers.add(new Worker(sockets.size() - 1, sockets.get(sockets.size() - 1)));
           // System.out.println(sockets.size());
            connection = true;
            //start another thread to listen to connections
            recivingMaster.start();
            //System.out.println("working - " + working +", location q - " + locationQ.peek());
            while (locationQ.size() == 0){
                try{
                    thread.sleep(10);
                }catch (Exception e){}
            }
            //System.out.println("i am working! for g and location "+locationQ.peek()+ "size"+locationQ.size());
            while (working) {
                try{
                    thread.sleep(10);
                }catch (Exception e){}
                if (locationQ.size() > 0) {
                    //System.out.println("start  to reading files");
                    String code = readFile( "src/"+locationQ.peek() + ".java"); // read the code
                    //System.out.println("reading files");
                    readFiles();

                    //gives the packet to the worker
                    //System.out.println("getting the worker");
                    workersMap.put(nameQ.peek(), getFreeWorker());
                    //System.out.println(workersMap.get(nameQ.peek()) + "have free worker");
                    workersMap.get(nameQ.peek()).send(new Gsons.Packet(true, code, locationQ.pop(), getInput(inputQ.pop()), sourcesQ.pop(), dependenciesQ.pop(), dependenciesFiles));
                    //System.out.println("sended the information");
                    scanner.close();
                    fileReader.close();
                }
            }

            try{
                for(int i = 0;i < sockets.size(); i++)
                    workers.get(i).close(closeType);
            }catch (IOException x){
                x.printStackTrace();
            }

            while (!recivingMaster.isDead()){
                (new Socket("127.0.0.1", port)).close(); // create a new socket to make him leave the serverSocket.accept()
            }
            serverSocket.close();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    /**
     * read the dependencies files
     */
    public void readFiles() throws IOException{
        if(sourcesQ.peek().length != dependenciesQ.peek().length){
            close(closeType);
            return;
        }
        dependenciesFiles = new String[sourcesQ.peek().length];
        for(int i = 0; i < sourcesQ.peek().length; i++){
            dependenciesFiles[i] = readFile(sourcesQ.peek()[i]+"/"+dependenciesQ.peek()[i]+".java");
        }
    }

    /**
     *
     * @param inputs strings
     * @return gson string of the inputs
     */
    public String getInput(String[] inputs){
        Gsons.Basic packet = new Gsons.Basic(inputs);
        return Gsons.gson.toJson(packet);
    }

    /**
     *
     * @param name a file name + location(from src not include) + type
     * @return a string of the input of the file, tested with java files, does not works with text normally
     * @throws IOException
     */
    public String readFile(@NotNull String name) throws IOException{
        if(name.length() < "/.java".length()+1)
            return "";
        String fileString = "";
        fileReader = new FileInputStream(name);
        scanner = new Scanner(fileReader);
        while (scanner.hasNextLine())
            fileString += scanner.nextLine() + "\n";
        return fileString;
    }

    /**
     * wait until mastersocket have a connection at least one worker
     */
    public void waitForConnection() {
        while (!connection) {
            try {
                Thread.sleep(100);
            }
            catch (Exception e){}
        }
    }

    /**
     * @return the first free active worker the functions finds
     */
    public Worker getFreeWorker() {
        boolean got = false;
        for (int i = lastBusy; i < workers.size(); i++) {
            if (!workers.get(i).isActive()) {
                lastBusy = i;
                return workers.get(i);
            }
        }
        while (!got) {
            for (int i = 0; i < workers.size(); i++) {
                if (workers.get(i) != null && !workers.get(i).isActive()) {
                    lastBusy = i;
                    return workers.get(i);
                }
            }
        }
        return null;
    }

    /**
     * wait for all the results of the workers to be received, this function BLOCKS the main thread
     */
    public void waitForResults() {
        String a ="";
        boolean wait = true;
        boolean check = false;
        int countNull = 0;
        while(wait){
            check = true;
            countNull = 0;
            for(int i = 0; i < workers.size(); i++){
                if(workers == null)
                    countNull ++;
                if(workers != null && !workers.get(i).isActive())
                    check = false;
            }
            if(countNull == workers.size())
                wait = true;
            if(check)
                wait = false;
        }
    }


    /**
     * ends the while loop
     */
    public void close(CloseType type){
        recivingMaster.close();
        working = false;
        closeType = type;
    }

    /**
     * the starter of the thread
     */
    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }

    /**
     * dont use
     * add a worker to the worker arrayList
     * @param socket   the connection with the worker
     * @throws IOException
     */
    @PrivateUse
    public void addWorker(Socket socket) throws IOException{
        sockets.add(socket);
        workers.add(new Worker(sockets.size()-1, sockets.get(sockets.size()-1)));
    }

    /**
     * send a mission to the worker
     * @param name the name of the call
     * @param location the name of the java code file
     * @param dependencies of the file, if it is both on the master and on the worker in src file put "" otherwise dont use!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param params string params to the code
     */
    public void giveMission(String name, String location,String[] sources, String[] dependencies, String... params){
        nameQ.push(name);
        locationQ.push(location);
        //System.out.println("location in master socket - " +locationQ.peek());
        dependenciesQ.push(dependencies);
        inputQ.push(params);
        sourcesQ.push(sources);
    }

    /**
     * return the answer of the name
     * @param name the code name which is gave in giveMission
     * @return the answer at a string format, if returned "" it means that you dont got any result or the process still goes on
     */
    public String getAnswer(String name) throws IOException{
        for(int i = 0; i<workersMap.size();i++){
            if(workersMap.get(name) == workers.get(i)){
                return workers.get(i).getInput();
            }
        }
        return "";
    }
}
