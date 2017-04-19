package ru.epavlov.avrdude;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by Eugene on 20.03.2017.
 */
public class Sketch {
    public  static  String sketch ;
    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                sketch ="C:\\modbusRTUJ\\target\\sketch\\";
        } else {
            sketch = "/usr/java/sketch/";
        }
    }


    private ProcessBuilderUtil util = new ProcessBuilderUtil();

    public ArrayList<String> getFiles(){
         System.out.println(sketch);
        ArrayList<String> files = new ArrayList<>();
        File f = new File(sketch);
        if (f.exists() && f.listFiles()!=null){
            for (File f_ :f.listFiles()){
                files.add(f_.getName());
            }
        }
        return files;
    }
    public void add(byte[] bytes, String name){
        File f = new File(sketch+name);
        try {
            Files.write(f.toPath(), bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delete(String fileName){
        File f = new File(sketch+fileName);
        if (f.exists()) f.delete();
    }
    public void upload(String fileName, String port,String plata)  {
        File f = new File(sketch+fileName);
        if (f.exists())
        util.startCommand(f.getPath(), port,plata);
    }
    public void stop(){
        util.stop();
    }
    public ArrayList<String> getLogs(){
        return util.getLog();
    }
    public boolean isUploading(){
        return util.inProcess();
    }
}
