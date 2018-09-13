package Distributed;

import java.util.ArrayList;

public class CompilationMaster {
    private static CompilationMaster instance;
    private ArrayList<ClassCreator> creators;
    private ArrayList<String> filesNames;
    private ArrayList<String> rootsNames;

    public static CompilationMaster init() {
        if(instance == null){
            instance = new CompilationMaster();
        }
        return instance;
    }

    private CompilationMaster(){
        filesNames = new ArrayList<>() ;
        rootsNames = new ArrayList<>();
        creators = new ArrayList<>();
    }

    /**
     * compile the java file into a class file
     * @param root the root of the file
     * @param name the name of the file
     */
    public void addFile(String root, String name){
        filesNames.add(name) ;
        rootsNames.add(root);
        creators.add(new ClassCreator(root, name));
        creators.get(creators.size()-1).start();
    }

    /**
     * compile the files from the same root to a lot of class files
     * @param root the root of the files
     * @param names the names of the files
     */
    public void addFiles(String root, String...names){
        for(int i = 0; i < names.length; i++){
            addFile(root, names[i]);
        }
    }

    /**
     *
     * @return if the compilation of the entirty of the files is done
     */
    public boolean isFinished(){
        for(ClassCreator c : creators){
            if(!c.isWorking())
                return false;
        }
        return true;
    }

    /**
     * freeing the memory which is taken by the compilation master later which it dosent neede.
     * dont use unless isFinished return true
     */
    public void close(){
        filesNames = null;
        rootsNames = null;
        creators = null;
    }
}
