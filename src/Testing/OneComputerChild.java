package Testing;

import GsonInformation.Gsons;
import Master.CloseType;
import Master.MasterDThread;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OneComputerChild extends MasterDThread {
    private MasterDThread master;
    private String[] fileList;

    public static void main(String[] args){
        OneComputerChild child = new OneComputerChild();
        child.start();
    }

    public OneComputerChild(String ... files){
        fileList = files;
    }

    @Override
    public void run(){
        System.out.println("start");
        // getting the time for the test
        final long startTime = System.currentTimeMillis();

        //setting the val
        String[] sources = {""};
        String[] dependencies = {""};

        final long end = 100000000; //range of the test

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(String.valueOf(end - 1).getBytes());
            Gsons.SHA256 sha256 = new Gsons.SHA256(0 , end, messageDigest);
            master.execute("a", "Distributed/Sha256Test", sources, dependencies, Gsons.gson.toJson(sha256));
        }catch (NoSuchAlgorithmException x){
            x.printStackTrace();
        }

        //waiting for the result from the workers
        master.waitForResults();

        //printing the result
        System.out.println("answer is: "+master.getAnswer("a"));

        //getting the ending time
        final long endTime = System.currentTimeMillis();
        final long time = endTime - startTime;

        //printing the time which the algorithem took
        System.out.println("To run the program its took min: " + (long)(time/1000/60) + ", sec: " + (long)(time/1000)%60 + ", milisec: " + (time %(60*1000)) % 1000);
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
