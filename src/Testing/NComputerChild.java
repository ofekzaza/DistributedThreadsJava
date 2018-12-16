package Testing;

import GsonInformation.Gsons;
import Master.CloseType;
import Master.MasterDThread;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NComputerChild extends MasterDThread {
    private MasterDThread master;
    private String[] fileList;

    public static void main(String[] args){
        NComputerChild child = new NComputerChild();
        child.start();
    }

    public NComputerChild(String ... files){
        fileList = files;
    }

    @Override
    public void run(){
        // getting the time for the test

        final long startTime = System.currentTimeMillis();

        //setting the val
        String[] sources = {""};
        String[] dependencies = {""};

        final long end = 100000000; //range of the test
        final short computers = 6;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(String.valueOf(end - 1).getBytes());
            for(int  i = 0; i < computers; i++) { // execute the program in a generic number of computers
                System.out.println(i);
                long start = i * end / computers;
                Gsons.SHA256 sha256 = new Gsons.SHA256(start, start + end / computers, messageDigest);
                master.execute("Sha256"+i, "Distributed/Sha256Test", sources, dependencies, Gsons.gson.toJson(sha256));
                System.out.println(start);
                System.out.println(start + end / computers);
            }
        }catch (NoSuchAlgorithmException x){
            x.printStackTrace();
        }

        //waiting for the result from the workers
        master.waitForResults();

        //printing the result
        for(int i = 0; i < computers; i++){
            String ans = master.getAnswer("Sha256"+i);
            if(ans != "-1" && ans != "") {
                System.out.println("The Answer is " + ans);
                System.out.println(i);
            }
        }
        //getting the ending time
        final long endTime = System.currentTimeMillis();
        final long time = endTime - startTime;

        //printing the time which the algorithem took
        System.out.println("To run the program its took min: " + (long)(time/1000/60) + ", sec: " + (long)(time/1000)%60 + ", milisec: " + (time % (60 * 1000)) % 1000);
        System.out.println("Total second: " + time /1000 + "." + time %1000);
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
