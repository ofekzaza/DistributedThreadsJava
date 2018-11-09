package Testing;

import Master.CloseType;
import Master.MasterDThread;
import Master.Worker;

public class Child extends MasterDThread {
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
        master.execute("Distributed/Example", sources, dependencies, "");
        System.out.println("have given the order");
        master.waitForResults();
        System.out.println("answer is: "+master.getAnswer("Distributed/Example"));
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
