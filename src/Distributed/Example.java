package Distributed;

public class Example extends DistributedFunction {

    public Example(){

    }

    public static void main(String[] args){
        Example example = new Example();
        example.start();
    }

    @Override
    public String execute(int[] ints, double[] doubles, boolean[] booleans, String[] strings){
        return "good";
    }

    @Override
    public void start(){
        DistributedFunction distributedFunction = new DistributedFunction(this);
        distributedFunction.start();
    }

}
