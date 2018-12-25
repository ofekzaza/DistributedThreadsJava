package Distributed;

public final class Example extends DistributedFunction {

    /**
     * the constructor is for the main, no one else should use him
     */
    private Example(){

    }

    /**
     * main of the class file
     * @param args
     */
    public static void main(String[] args){
        Example example = new Example();
        example.start();
    }

    /**
     * the code that the user will program
     * this will run on the other computer
     * @param strings string inputs
     * @return a String and send him back to the master
     */
    @Override
    public String run(String[] strings){
        return "good this is good";
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

