package GsonInformation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * adapter class
 */
public class Gsons {
    public static Gson gson = new Gson();

    public static class Basic{
        public String[] strings;

        public Basic(String... params){
            strings = params;
        }

        @Override
        public String toString(){
            return Gsons.toString(strings);
        }
    }

    public static class Packet {
        public String information;
        public boolean alive;
        public String code;
        public String name;
        public String[] dependencies;
        public String[] sources;
        public String[] dependenciesFiles;

        public Packet(boolean alive, String code, String name, String information, String[] sources, String[] dependencies, String[] dependenciesFiles){
            this.information = information;
            this.alive = alive;
            this.code = code;
            this.name = name;
            this.dependencies = dependencies;
            this.sources = sources;
            this.dependenciesFiles = dependenciesFiles;
        }

        @Override
        public String toString(){
            return "Name: "+name+", Code: "+code+ ", Alive: "+alive+", information: "+information+", sources: "+Gsons.toString(sources)+", dependencies: "+Gsons.toString(dependencies);
        }

        /**
         * @param strs array of strings
         * @return a tostring of an array
         */
    }

    public static String toString(String... strs){
        String str = "";
        if(strs.length > 0)
            str += 0 +": "+strs[0];
        for(int i = 1; i< strs.length; i++)
            str += ", " + i +": "+strs[i];
        return str;
    }

    public static final TypeAdapter<GsonInformation.Gsons.Packet> JSON_CACHE_PARSER_PACKET = gson
            .getAdapter(GsonInformation.Gsons.Packet.class);
    public static final TypeAdapter<Basic> JSON_CACHE_PARSER_BASIC = Gsons.gson
            .getAdapter(GsonInformation.Gsons.Basic.class);
}
