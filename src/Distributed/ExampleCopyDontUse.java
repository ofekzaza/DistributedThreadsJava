package Distributed;

public final class ExampleCopyDontUse extends DistributedFunction {

    /**
     * the constructor is for the main, no one else should use him
     */
    private ExampleCopyDontUse(){

    }

    /**
     * main of the class file
     * @param args
     */
    public static void main(String[] args){
        ExampleCopyDontUse example = new ExampleCopyDontUse();
        example.start();
    }

    /**
     * the code that the user will program
     * this will run on the other computer
     * @param strings string inputs
     * @return a String and send him back to the master
     */
    @Override
    public String execute(String[] strings){
        System.out.println("good");
        return "good";
    }

    /**
     * the start of the father
     */
    @Override
    public void start(){
        DistributedFunction distributedFunction = new DistributedFunction(this);
        distributedFunction.start();
    }

}
