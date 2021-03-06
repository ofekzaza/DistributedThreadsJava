package Master;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;

public class IpBroadcast extends Thread{
    private Thread thread;
    private static DatagramSocket socket = null;
    private String selfIp;
    private boolean working = true;
    private int port = 1861;
    private boolean broadcast = false;

    public IpBroadcast(){

    }

    public IpBroadcast(int port){

    }

    @Override
    public void run() {
        try {
             // the socket will broadcast, truly impressive
           // System.out.println("have broadcast socket");
            byte[] buffer = selfIp.getBytes();


            //System.out.println("starting to broadcasting");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 1861);


            while(working) {
                socket.send(packet);
                Thread.sleep(500);
            }

            socket.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, "Ipbroadcast");
            try { // init of broadcast socket
                selfIp = Inet4Address.getLocalHost().getHostAddress();
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            } catch (Exception x) {
                x.printStackTrace(); /// wellll what the fuck you done?!?!?!??!?
            }
            thread.start();

        }
    }

    public void waitForBroadcast(){
        while(!broadcast) {}
    }


    public void kill(){
        working = false;
        try{
            socket.close();
        }catch (Exception x){

        }
    }
}
