package Testing;

import GsonInformation.Gsons;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class hasing {
    public static final int maxInt = 2147483647;
                                     // 418956595
    public static void main(String[] args) throws  Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        final long end = 2000000;
        long a = 8888;
        byte[] messageDigest = md.digest(String.valueOf(a).getBytes());
        System.out.println(new String(messageDigest));
        //md = MessageDigest.getInstance("SHA-256");
        messageDigest = md.digest(String.valueOf(a).getBytes());
        System.out.println(new String(messageDigest));
        Gsons.SHA256 sha256 = new Gsons.SHA256(0 , end, messageDigest);
        System.out.println(md.digest(String.valueOf(a).getBytes()));
        System.out.println(test(Gsons.gson.toJson(sha256)));
    }

    public static String test(String... args){
        try {
            Gsons.SHA256 json = Gsons.JSON_CACHE_PARSER_SHA256.fromJson(args[0]);
            final long startPoint = json.start;
            final long endingPoint = json.end;
            final byte[] checksum = json.result;
            byte[] result;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                for (long i = startPoint; i < endingPoint; i++) {
                    result = md.digest(String.valueOf(i).getBytes());
                    if(check(result, checksum))
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

    public static boolean check(byte[] result, byte[] checksum){
        for(int x = 0; x < result.length; x++)
            if (result[x] != checksum[x])
                return false;
        return true;
    }

    public static byte[] sha256(MessageDigest md, String input) throws NoSuchAlgorithmException {
        byte[] messageDigest = md.digest(input.getBytes());
        return messageDigest;
    }


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
