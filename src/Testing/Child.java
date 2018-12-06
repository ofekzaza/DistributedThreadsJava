package Testing;

import Master.CloseType;
import Master.MasterDThread;

import java.util.zip.CRC32;

public class  Child extends MasterDThread {
    private MasterDThread master;
    private String[] fileList;

    public static void main(String[] args){
        Child child = new Child();
        child.start();
    }

    public Child(String ... files){
        fileList = files;
    }

    @Override
    public void run(){
        System.out.println("child main");
        String[] sources = {""};
        String[] dependencies = {""};
        System.out.println(sources.length);
        System.out.println(dependencies.length);
        CRC32 crc = new CRC32();
        crc.update(String.valueOf(2147483646).getBytes());
        master.execute("Distributed/CRC32Test", sources, dependencies, "0", "2147483647", "" + crc.getValue());
        System.out.println("have given the order");
        master.waitForResults();
        System.out.println("answer is: "+master.getAnswer("Distributed/CRC32Test"));
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
