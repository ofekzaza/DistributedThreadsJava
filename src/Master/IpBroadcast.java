package Master;

import Worker.WorkerMain;

import java.io.IOException;
import java.net.*;
import java.nio.Buffer;

public class IpBroadcast extends Thread{
    private Thread thread;
    private static DatagramSocket socket = null;
    private String selfIp;
    private boolean working = true;
    private int port = 1861;

    public IpBroadcast(int port){
        this.port = port;
    }

    public IpBroadcast(){

    }

    @Override
    public void run() {
        try {
            selfIp = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception x) {
            x.printStackTrace(); /// wellll what the fuck you done?!?!?!??!?
        }
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true); // the socket will broadcast

            byte[] buffer = selfIp.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 1861);


            while(working) {
                socket.send(packet);
                Thread.sleep(500);
            }

            socket.close();
        }catch (Exception x){
            x.printStackTrace();
        }
        System.out.println("broadcast is dead");
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, "Ipbroadcast");
            thread.start();
        }
    }

    public void kill(){
        working = false;
        try{
            socket.close();
        }catch (Exception x){

        }
    }
}
