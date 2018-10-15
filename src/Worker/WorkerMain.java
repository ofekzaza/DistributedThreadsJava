package Worker;

import GsonInformation.Gsons;
import com.google.gson.Gson;
import org.json.JSONException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class WorkerMain {
    private String serverIp = "";
    private Socket socket;
    private DataInputStream inputStream;
    private Scanner scanSocket;
    private DataOutputStream outputStream;
    private PrintStream printStream;
    private final int port = 1948;
    private FileOutputStream classFileOutputStream;
    private FileOutputStream inputFileOutputStream;
    private FileInputStream answerFileInputStream;
    private BufferedInputStream dataInputStream;
    private FileOutputStream answerFileOutputStream;
    private Scanner answerFileScanner;
    private Gsons.Packet jsonInput;
    private Gson gson;
    private Runtime runtime;

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
        WorkerMain worker = new WorkerMain();
        worker.start();
    }

    /**
     * constructor
     * creates IO connections with storage files
     * @throws IOException
     */
    public WorkerMain() throws IOException{
        Gson gson = Gsons.gson;
        inputFileOutputStream = new FileOutputStream("src/Worker/input.txt");
        answerFileInputStream = new FileInputStream("src/Worker/answer.txt");
        answerFileScanner = new Scanner(answerFileInputStream);
        answerFileOutputStream = new FileOutputStream("src/Worker/answer.txt");
        runtime = Runtime.getRuntime();
    }

    /**
     * waits for broadcast from the master which tells his ip
     * @throws IOException
     */
    public void catchMasterIp() throws IOException{
        DatagramSocket dataSocket = new DatagramSocket(1861);
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        dataSocket.receive(packet);
        serverIp = new String(packet.getData());
        dataSocket.close();
        System.out.println("Master ip is "+serverIp);
    }

    /**
     * creates tcp ip communication with the master
     * @throws IOException
     */
    public void establishTcpIpCommunications() throws IOException{
        boolean trying = true;
        while(trying) {
            try{
                System.out.println("trying to establish communications");
                socket = new Socket(serverIp, port);
                trying = false;
            }catch (Exception x){
                trying = true;
            }
        }
        dataInputStream = new BufferedInputStream(socket.getInputStream());
        scanSocket = new Scanner(dataInputStream);
        printStream = new PrintStream(socket.getOutputStream());
        System.out.println("establised communication !");
    }

    /**
     * this method waits for input from the user;
     */
    public boolean waitForInput() throws IOException{
        System.out.println("waiting for input");
        String str = "";

        while(!scanSocket.hasNext()){}

        System.out.println("are you killing me?");
        str += scanSocket.nextLine();

       // while(scanSocket.hasNextLine()){
         //   str += scanSocket.nextLine();
        //}

        System.out.println("wtf");
        System.out.println(str);
        System.out.println("have input from the master");

        jsonInput = Gsons.JSON_CACHE_PARSER.fromJson(str);



        System.out.println("wait for input is done "+ jsonInput.alive );
        return jsonInput.alive;
    }

    public boolean test(){
        System.out.println("maybe ");
        return scanSocket.hasNext();
    }

    /**
     * takes the json input and write it into files
     * @throws JSONException
     * @throws IOException
     */
    public void writeInput() throws JSONException, IOException, InterruptedException{
        System.out.println("name is  "+jsonInput.name);
        classFileOutputStream = new FileOutputStream("src/Distributed/"+jsonInput.name+".java");
        System.out.println("well");
        classFileOutputStream.write(jsonInput.code.getBytes());
        System.out.println(jsonInput.code);
        Process p = runtime.exec("javac -cp "+jsonInput.dependencies+"java-json.jar;src src/Distributed/"+jsonInput.name+".java");
        p.waitFor();
        System.out.println("good");
        inputFileOutputStream.write(jsonInput.information.getBytes());
        System.out.println("Wrote the input");
    }

    /**
     * excute the class file
     */
    public void execute() throws InterruptedException, IOException{
        Process e = runtime.exec("java -cp "+jsonInput.dependencies+"java-json.jar;src Distributed."+jsonInput.name.replace("/", "."));
        e.waitFor();
        System.out.println("finished executing the class file");
    }

    /**
     * read the answer from the answer file and send the answer back to the master
     */
    public void returnAnswer()throws IOException{
        String str = "";
        while(answerFileScanner.hasNext()){
            str +=answerFileScanner.next();
        }
        System.out.println("the answer is "+str);

        printStream.println(str);
        printStream.flush();
        System.out.println("sended the answer to the master");
    }

    /**
     * sending death message and closing everything
     * close the IO connections
     */
    public void destructor() throws IOException {
        //sends death message
        printStream.println("kill");
        printStream.flush();
        //clean space
        //close io connections
        answerFileScanner.close();
        answerFileOutputStream.close();
        answerFileInputStream.close();
        inputFileOutputStream.close();
        classFileOutputStream.close();
        scanSocket.close();
        printStream.close();
        socket.close();
    }
    /**
     * main of the worker
     * @throws IOException using sockets
     */
    public void start() throws IOException, JSONException, InterruptedException{
        catchMasterIp();
        establishTcpIpCommunications();
        while(waitForInput()){
            System.out.println("started the loop");
            writeInput();
            System.out.println("wrote the input");
            execute();
            System.out.println("finish executing");
            returnAnswer();
            System.out.println("returned the output");
        }
        destructor();
        System.out.println("worker is dead");
    }

}
