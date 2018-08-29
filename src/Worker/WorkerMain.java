package Worker;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class WorkerMain {
    private static String serverIp = "";

    public static void main(String[] args) throws IOException
    {
    }

    public static void helper() throws  Exception{
        DatagramSocket socket = new DatagramSocket(1861);

        byte[] buf = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.receive(packet);

        serverIp = new String(packet.getData());

        socket.close();
        System.out.println("Server ip is "+serverIp);
    }

}
