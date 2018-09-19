package Worker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class WorkerMain {
    private String serverIp = "";
    private Socket socket;
    private InputStream inputStream;
    private Scanner scanSocket;
    private OutputStream outputStream;
    private PrintStream printStream;
    private final int port = 1948;
    private FileOutputStream classFileOutputStream;
    private FileOutputStream inputFileOutputStream;
    private FileInputStream answerFileInputStream;
    private FileOutputStream answerFileOutputStream;
    private Scanner answerFileScanner;
    private JSONObject jsonObject;
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
        DatagramSocket socket = new DatagramSocket(1861);
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        serverIp = new String(packet.getData());
        socket.close();
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
                socket = new Socket(serverIp, port);
                trying = false;
            }catch (Exception x){
                trying = true;
            }
        }
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        scanSocket = new Scanner(inputStream);
        printStream = new PrintStream(outputStream);
        System.out.println("establised communication !");
    }

    /**
     * this method waits for input from the user;
     */
    public boolean waitForInput() throws JSONException{
        while(!scanSocket.hasNext()){}
        jsonObject = new JSONObject(scanSocket.next());
        System.out.println("have input from the master");
        return jsonObject.getBoolean("Working");
    }

    /**
     * takes the json input and write it into files
     * @throws JSONException
     * @throws IOException
     */
    public void writeInput() throws JSONException, IOException{
        classFileOutputStream = new FileOutputStream("src/Distributed/"+jsonObject.getString("Name")+".class");
        classFileOutputStream.write(jsonObject.getString("ClassFile").getBytes());
        inputFileOutputStream.write(jsonObject.getJSONObject("Input").toString().getBytes());
        System.out.println("Wrote the input");
    }

    /**
     * excute the class file
     */
    public void execute() throws InterruptedException, JSONException, IOException{
        Process e = runtime.exec("java -cp src Distributed."+jsonObject.getString("Name").replace("/", "."));
        e.waitFor();
        System.out.println("finished executing the class file");
    }

    /**
     * read the answer from the answer file and send the answer back to the master
     */
    public void returnAnswer()throws IOException{
        printStream.print(answerFileScanner.next());
        if(false) {
            answerFileOutputStream.write("".getBytes());
            inputFileOutputStream.write("".getBytes());
            classFileOutputStream.write("".getBytes());
        }
        System.out.println("sended the answer to the master");
    }

    /**
     * close the IO connections
     */
    public void destructor() throws IOException {
        answerFileScanner.close();
        answerFileOutputStream.close();
        answerFileInputStream.close();
        inputFileOutputStream.close();
        classFileOutputStream.close();
        scanSocket.close();
        printStream.close();
        inputStream.close();
        outputStream.close();
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
            writeInput();
            execute();
            returnAnswer();
        }
        destructor();
        System.out.println("worker is dead");
    }

}
