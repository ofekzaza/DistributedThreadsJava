package Util;

public class TcpPacket <T> {
    public byte[] classFile;
    public boolean working;
    public T[] params;

    public TcpPacket(byte[] file, boolean work, T... params){
        classFile = file;
        working = work;
        this.params = params;
    }

    public TcpPacket(byte[] file, T[] params, boolean work){
        classFile = file;
        working = work;
        this.params = params;
    }
}
