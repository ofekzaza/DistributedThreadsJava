package Distributed;

import GsonInformation.Gsons;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class Sha256Test extends DistributedFunction {
    byte[] checksum;

    public static void main(String[] args){
        Sha256Test self = new Sha256Test();
        self.start();
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
        try {
            Gsons.SHA256 json = Gsons.JSON_CACHE_PARSER_SHA256.fromJson(args[0]);
            final long startPoint = json.start;
            final long endingPoint = json.end;
            checksum = json.result;
            byte[] result;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                for (long i = startPoint; i < endingPoint; i++) {
                    result = md.digest(String.valueOf(i).getBytes());
                    if(check(result))
                        return String.valueOf(i);
                }
            } catch (NoSuchAlgorithmException x){
                x.printStackTrace();
            }
        }catch (IOException x){
            x.printStackTrace();
        }
        return "-1";
    }

    private boolean check(byte[] result){
        for(int x = 0; x < result.length; x++)
            if (result[x] != checksum[x])
                return false;
        return true;
    }

    @Override
    public void start(){
        DistributedFunction father = new DistributedFunction(this);
        father.start();
    }

}
