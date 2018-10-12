package Master;

import GsonInformation.Gsons;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * singalton responsible from communication with the workers
 */
public final class MasterSocket extends Thread{
    private Thread thread;
    private String name = "MasterSocket";
    private final static MasterSocket instance = new MasterSocket();
    private ServerSocket serverSocket;
    private final int port = 1948; // dont change
    private boolean working;
    public ArrayList<Socket> sockets;
    private ArrayList<Worker> workers;
    public static String[] JsonNames = {"Ints", "Doubles", "Booleans", "Strings"};
    private PriorityQueue<String> nameQ;
    private PriorityQueue<String[]> inputQ;
    private PriorityQueue<String> dependenciesQ;
    private WaitForWorkers recivingMaster;
    private Scanner scanner;
    private int lastBusy;
    private HashMap<String, Worker> workersMap;

    private MasterSocket(){
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException x){
            x.printStackTrace();
        }
        lastBusy = 0;
        working = true;
        sockets = new ArrayList<Socket>();
        workers = new ArrayList<Worker>();
        nameQ = new PriorityQueue<String>();
        inputQ = new PriorityQueue<String[]>();
        workersMap = new HashMap<>();
        dependenciesQ = new PriorityQueue<String>();
        recivingMaster = new WaitForWorkers(serverSocket, port);
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

    /**
     * function main
     */
    @Override
    public void run() {
        try {
            //wait to get the first connection
            sockets.add(serverSocket.accept());
            workers.add(new Worker(sockets.size()-1, sockets.get(sockets.size()-1)));
            System.out.println("got connection");

            //start another thread to listen to connections
            recivingMaster.start();

            while (working) {

                if(nameQ.size() > 0){
                    String code = "";
                    scanner = new Scanner("src/Distributed/"+nameQ.peek()+".java");
                    while(scanner.hasNextLine())
                        code+=scanner.nextLine();
                    JSONArray array = new JSONArray();
                    String[] strs = inputQ.poll();
                    for(int i = 0; i < strs.length; i++)
                        array.put(strs[i]);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("Strings", array);
                    }catch (JSONException e){}
                    //gives the packet to the worker
                    workersMap.put(nameQ.peek(), getFreeWorker());
                    workersMap.get(nameQ.peek()).send(new Gsons.Packet(true, nameQ.poll(), code, object.toString(), dependenciesQ.poll()));
                }
            }

            for(int i = 0;i < workers.size(); i++)
                workers.get(i).close();

            serverSocket.close();
        }catch (IOException x){
            x.printStackTrace();
        }
        System.out.println("master socket is dead");
    }

    /**
     *
     * @return the first free active worker the functions finds
     */
    public Worker getFreeWorker(){
        boolean got = false;
        for (int i = lastBusy; i < workers.size(); i++){
            if(!workers.get(i).isActive()) {
                lastBusy = i;
                return workers.get(i);
            }
        }
        while (!got) {
            for (int i = 0; i < workers.size(); i++){
                if(!workers.get(i).isActive()) {
                    lastBusy = i;
                    return workers.get(i);
                }
            }
        }
        return null;
    }

    /**
     * wait for all the results of the workers to be received, this function __BLOCKS__ the main thread
     */
    public void waitForResults() {
        try {
            for(Worker w : workers){
                while(w.getInput() != "" && w.isActive()){ }
            }
        }catch (Exception x){}
    }

    /**
     * ends the while loop
     */
    public void close(){
        working = false;
        try{
            for(int i = 0;i < sockets.size(); i++)
                workers.get(i).close();
        }catch (IOException x){
            x.printStackTrace();
        }
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
    public void giveMission(String name, String dependencies, String... params){
        nameQ.add(name);
        dependenciesQ.add(dependencies);
        inputQ.add(params);
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
