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
        master.execute("Example", "","");
        System.out.println("have givven the order");
        master.waitForResults();
        System.out.println(master.getAnswer("Example"));
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this, CloseType.Kill, fileList);
            master.start();
        }
    }
}
