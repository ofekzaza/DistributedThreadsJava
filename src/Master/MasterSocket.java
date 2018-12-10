package Master;

import GsonInformation.Gsons;
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
    private Thread thread;
    private String name = "MasterSocket";
    private final static MasterSocket instance = new MasterSocket();
    private ServerSocket serverSocket;
    private final int port = 1948; // dont change
    private boolean working;
    public ArrayList<Socket> sockets;
    private ArrayList<Worker> workers;
    public static String[] JsonNames = {"Ints", "Doubles", "Booleans", "Strings"};
    private LinkedList<String> nameQ;
    private LinkedList<String[]> inputQ;
    private LinkedList<String[]> dependenciesQ;
    private LinkedList<String[]> sourcesQ;
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
        inputQ = new LinkedList<>();
        sourcesQ = new LinkedList<>();
        workersMap = new HashMap<>();
        dependenciesQ = new LinkedList<>();
        recivingMaster = new WaitForWorkers(serverSocket, port);
        System.out.println("Good at least");
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
            sockets.add(serverSocket.accept());
            workers.add(new Worker(sockets.size() - 1, sockets.get(sockets.size() - 1)));
            connection = true;

            //start another thread to listen to connections
            recivingMaster.start();

            while (working) {

                if (nameQ.size() > 0) {
                    String code = readFile( "src/"+nameQ.peek() + ".java"); // read the code

                    readFiles();

                    //gives the packet to the worker
                    workersMap.put(nameQ.peek(), getFreeWorker());
                    workersMap.get(nameQ.peek()).send(new Gsons.Packet(true, code, nameQ.pop(), getInput(inputQ.pop()), sourcesQ.pop(), dependenciesQ.pop(), dependenciesFiles));
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
    public String readFile(String name) throws IOException{
        System.out.println(name);
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
                if (!workers.get(i).isActive()) {
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
        boolean True = true;
        boolean check = false;
        while(True){
            check = true;
            for(int i = 0; i < workers.size(); i++){
                if(!workers.get(i).isActive())
                    check = false;
            }
            if(check)
                break;
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
    public void addWorker(Socket socket) throws IOException{
        sockets.add(socket);
        workers.add(new Worker(sockets.size()-1, sockets.get(sockets.size()-1)));
    }

    /**
     * send a mission to the worker
     * @param name the name of the java code file
     * @param dependencies of the file, if it is both on the master and on the worker in src file put "" otherwise dont use!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param params string params to the code
     */
    public void giveMission(String name,String[] sources, String[] dependencies, String... params){
        nameQ.push(name);
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
