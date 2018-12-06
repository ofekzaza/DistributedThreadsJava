package Testing;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class hasing {
    public static final int maxInt = 2147483647;
                                     // 418956595
    public static void main(String[] args){
        CRC32 b = new CRC32();
        int c = (int)(2);
        b.update(String.valueOf(c).getBytes());
        System.out.println("origin: " + c);
        long l = b.getValue();
        System.out.println("encrypted: " + l);
        b.reset();
        System.out.println(b.getValue()); // 3518238081
        System.out.println("decrypted: " + function(l, c));
    }

//    public static String readFile(String name) throws Exception{
//        String s;
//        DataInputStream stream = new DataInputStream(new FileInputStream(name));
//        while(strea)
//        return s;
//    }

    public static int function(long b, int origin){
        CRC32 crc = new CRC32();
        crc.update(origin);
        System.out.println("Encription: "+crc.getValue());
        crc.reset();
        int startPoint = new Integer(0);
        final int endingPoint = new Integer(maxInt);
        final long checksum = new Long(b);
        for(int i = startPoint; i < endingPoint; i++){
            crc.update(String.valueOf(i).getBytes());
            if(crc.getValue() == checksum) {
                System.out.println("idk: "+crc.getValue());
                return i;
            }
            crc.reset();
        }
        return -1;
    }
}
