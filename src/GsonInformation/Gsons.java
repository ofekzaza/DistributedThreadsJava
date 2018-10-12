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
        public String dependencies;

        public Packet(boolean Alive, String Code, String Name, String Information, String dependencies){
            information = Information;
            alive = Alive;
            code = Code;
            name = Name;
            this.dependencies = dependencies;
        }

        @Override
        public String toString(){
            return "Name: "+name+", Code: "+code+ ", Alive: "+alive+", information: "+information+", dependencies: "+dependencies;
        }
    }

    public static final TypeAdapter<GsonInformation.Gsons.Packet> JSON_CACHE_PARSER = gson
            .getAdapter(GsonInformation.Gsons.Packet.class);
}
