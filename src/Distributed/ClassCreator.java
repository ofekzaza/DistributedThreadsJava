package Distributed;

import java.io.IOException;

public class ClassCreator extends Thread {
    /**
     * name of the file, and it include the package
     * the root is the file from the generic source to the first package
     */
    private String name;
    private String root;
    private Thread thread;
    private boolean working;

    public ClassCreator(String root, String name){
        this.name = name;
        this.root = root;
        working = true;
    }

    /**
     * takes the file from the root and transfare it into a class file
     */
    @Override
    public void run(){
        try{
            Runtime r = Runtime.getRuntime();
            try{
                Process p = r.exec("javac "+root+"/"+name+".java");
                p.waitFor();
                p.destroy();
                r = null;
                working = false;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    public boolean isWorking(){
        return working;
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, root+"/"+name);
            thread.start();
        }
    }
}
