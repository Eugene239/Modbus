package ru.epavlov.avrdude;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Eugene on 07.03.2017.
 */
public class FilesUtil {
    public final static String getOutPath = FilesUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public ArrayList<File> getSketches() throws Exception {
        File f = new File(getOutPath+"sketches");
        if (f.exists() && f.isDirectory()){
            ArrayList<File> list= new ArrayList<>();
            for(File f_: f.listFiles()){
                list.add(f_);
            }
            return list;
        }else {
            //return Response.noContent().build();
            throw new Exception("Нет папки со скетчами");
        }
    }
    private boolean checkMakeFile(File f){
        for (File f_: f.listFiles()){
            if(f_.getName().equals("Makefile"))
                return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        FilesUtil filesUtil = new FilesUtil();
        filesUtil.getSketches().forEach(file -> {
            System.out.println(file.getName()+" "+ filesUtil.checkMakeFile(file));
        });
    }
}
