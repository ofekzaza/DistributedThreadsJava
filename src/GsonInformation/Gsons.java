package GsonInformation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

public class Gsons {
    public static Gson gson = new Gson();

    public static class Packet {
        public String information;
        public boolean alive;
        public String code;
        public String name;

        public Packet(boolean a, String c, String n, String i){
            information = i;
            alive = a;
            code = c;
            name = n;
        }

        @Override
        public String toString(){
            return "Name: "+name+", Code: "+code+ ", Alive: "+alive+", information: "+information;
        }
    }

    public static final TypeAdapter<GsonInformation.Gsons.Packet> JSON_CACHE_PARSER = gson
            .getAdapter(GsonInformation.Gsons.Packet.class);
}
