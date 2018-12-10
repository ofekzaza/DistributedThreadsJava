package Testing;

import GsonInformation.Gsons;
import Master.CloseType;
import Master.MasterDThread;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        final long end = 2000000;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(String.valueOf(8888).getBytes());
            Gsons.SHA256 sha256 = new Gsons.SHA256(0 , end, messageDigest);
            master.execute("Distributed/Sha256Test", sources, dependencies, Gsons.gson.toJson(sha256));

        }catch (NoSuchAlgorithmException x){
            x.printStackTrace();
        }
        System.out.println("have given the order");
        master.waitForResults();
        System.out.println("answer is: "+master.getAnswer("Distributed/Sha256Test"));
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
