package Distributed;

import java.util.zip.CRC32;

public class CRC32Test extends DistributedFunction {
    CRC32 crc;

    public static void main(String[] args){
        CRC32Test self = new CRC32Test();
        self.start();
    }

    public CRC32Test(){
        crc = new CRC32();
    }

    /**
     * this function brute force crc32 checksum, if found the value return the number, otherwise return -1
     * @param args string inputs
     *             0 - the starting point of the search
     *             1 - the ending point of the search
     *             2 - the wanted checksum
     * @return
     */
    @Override
    public String run(String[] args){
        int startPoint = new Integer(args[0]);
        final int endingPoint = new Integer(args[1]);
        final long checksum = new Long(args[2]);
        for(int i = startPoint; i < endingPoint; i++){
            crc.update(String.valueOf(i).getBytes());
            if(crc.getValue() == checksum) {
                return String.valueOf(i);
            }
        }
        return "-1";
    }

    @Override
    public void start(){
        DistributedFunction father = new DistributedFunction(this);
        father.start();
    }

}
