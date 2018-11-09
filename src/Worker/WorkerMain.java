package Worker;

import GsonInformation.Gsons;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class WorkerMain {
    private String serverIp = "";
    private Socket socket;
    private Scanner scanSocket;
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
    private boolean working;

    public static void main(String[] args) throws IOException, InterruptedException {
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
        working = true;
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

        str += scanSocket.nextLine();

        System.out.println("have input from the master");

        jsonInput = Gsons.JSON_CACHE_PARSER_PACKET.fromJson(str);

        if(false) {
            System.out.println("code " + jsonInput.code);
            System.out.println("sources " + Gsons.toString(jsonInput.sources));
            System.out.println("dependancies " + Gsons.toString(jsonInput.dependencies));
            System.out.println("Name " + jsonInput.name);
            System.out.println("inforamtion " + jsonInput.information);
        }

        System.out.println("wait for input is done "+ jsonInput.alive );
        if(!jsonInput.alive)
            switch (jsonInput.information){
                case "kill":
                    working = false;
                case "prepare":
            }
        return jsonInput.alive;
    }

    /**
     * takes the json input and write it into files
     * @throws IOException
     */
    public void writeInput() throws IOException, InterruptedException{
        System.out.println("name is  "+jsonInput.name);
        classFileOutputStream = new FileOutputStream("src/"+jsonInput.name+".java");
        System.out.println("well");
        classFileOutputStream.write(jsonInput.code.getBytes());
        System.out.println(jsonInput.code);
        Process p = runtime.exec("javac -cp src;gson-2.7.jar src/"+jsonInput.name+".java"); // compile the java file into class file
        p.waitFor();
        inputFileOutputStream.write(jsonInput.information.getBytes());
    }

    /**
     * excute the class file
     */
    public void execute() throws InterruptedException, IOException{
        Process e = runtime.exec("java -cp gson-2.7.jar;src "+jsonInput.name.replace("/", ".")); // run the class file
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

    public void continuationDestructor() throws IOException{
        printStream.println("kill");
        printStream.flush();

        classFileOutputStream.close();

        scanSocket.close();
        printStream.close();
        socket.close();
    }

    /**
     * main of the worker
     * @throws IOException using sockets
     */
    public void start() throws IOException, InterruptedException{
        while(working) {
            catchMasterIp();
            establishTcpIpCommunications();
            while (waitForInput()) {
                System.out.println("started the loop");
                writeInput();
                System.out.println("wrote the input");
                execute();
                System.out.println("finish executing");
                returnAnswer();
                System.out.println("returned the output");
            }
            continuationDestructor();
        }
        destructor();
        System.out.println("worker is dead");
    }

}
