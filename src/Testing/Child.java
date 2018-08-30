package Testing;

import Master.MasterDThread;

public class Child extends MasterDThread {
    MasterDThread master;

    public static void main(String[] args){
        Child child = new Child();
        child.start();
    }

    public Child(){

    }

    @Override
    public void run(){
        System.out.println("child main");
    }

    @Override
    public void start(){
        if(master == null){
            master = new MasterDThread(this);
            master.start();
        }
    }
}
