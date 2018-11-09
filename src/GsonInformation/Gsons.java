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

    public static class Basic{
        public String[] strings;
        public String tostring;

        public Basic(String... params){
            strings = params;
        }

        @Override
        public String toString(){
            if(tostring == null){
                tostring = "";
                if(strings.length > 0)
                    tostring += 0 +": "+strings[0];
                for(int i = 1; i < strings.length; i++){
                    tostring += ", "+i+": "+strings[i];
                }
            }
            return tostring;
        }
    }

    public static final TypeAdapter<GsonInformation.Gsons.Packet> JSON_CACHE_PARSER_PACKET = gson
            .getAdapter(GsonInformation.Gsons.Packet.class);
    public static final TypeAdapter<GsonInformation.Gsons.Basic> JSON_CACHE_PARSER_BASIC = gson
            .getAdapter(GsonInformation.Gsons.Basic.class);
}
