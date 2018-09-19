package Worker;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class WorkerMain {
    private String serverIp = "";
    private Socket socket;
    private final int port = 1948;

    public static void main(String[] args) throws IOException
    {
        WorkerMain worker = new WorkerMain();
        worker.start();
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
     * creats tcp io communication with the master
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
        System.out.println("establised communication !");
    }

    /**
     * this method waits for input from the user;
     */
    public boolean waitForInput(){
        return false;
    }

    public void execute(){

    }

    public void returnAnswer(){

    }

    /**
     * main of the worker
     * @throws IOException using sockets
     */
    public void start() throws IOException{
        catchMasterIp();
        establishTcpIpCommunications();
        while(waitForInput()){
            execute();
            returnAnswer();
        }
        System.out.println("worker is dead");
    }

}
