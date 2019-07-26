package Testing;

import GsonInformation.Gsons;
import Master.CloseType;
import Master.MasterDThread;

public class Example extends MasterDThread {
    private MasterDThread masterDThread;

    public static void main(String[] args){
        Example example = new Example();
        example.start();
        System.out.println("well");
    }

    @Override
    public void run(){
        String[] sources = {""};
        String[] dependencies = {""};
        Gsons.Basic basic = new Gsons.Basic("value");
        System.out.println("init");
        masterDThread.execute("Example", "Distributed/Example", sources, dependencies, Gsons.gson.toJson(basic));

        masterDThread.waitForResults();

        System.out.println(masterDThread.getAnswer("Example"));
    }

    @Override
    public void start(){
        if(masterDThread == null){
            System.out.println("start");
            masterDThread = new MasterDThread(this, CloseType.Kill);
            System.out.println("master exist");
            masterDThread.start();
        }
    }

}

