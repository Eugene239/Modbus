package ru.epavlov.avrdude;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Eugene on 07.03.2017.
 */
public class ProcessBuilderUtil {
   static  private ArrayList<String> log = new ArrayList<>();
    static Process p;
    static Thread thread;
    static int lastread=0;

    private void startCommand() throws IOException {
        //ProcessBuilder pb = new ProcessBuilder("ipconfig");
        if (thread==null || !thread.isAlive()) {
            thread = new Thread(() -> {
                try {
                    p = Runtime.getRuntime().exec("ipconfig");
                    readWithIS();
                    System.out.println("[===ENDED===]");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }else {
            System.err.println("ONE PROCESS IN PROGRESS");
        }
        //new Thread(thread).start();
    }
    void readWithIS() throws IOException {
        InputStream is  = p.getInputStream();
        //String s;
        int val;
        String s="";
        while ((val=is.read())!=-1) {
            if ((char)val!='\n') s+=(char)val;
            else {
                log.add(new String(s.getBytes(), Charset.defaultCharset()));
                s="";
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilderUtil pb = new ProcessBuilderUtil();
        pb.startCommand();
       // pb.startCommand();
        while (thread.isAlive()){
            int size = log.size();
            for (int i= lastread;i<size;i++){
               // System.out.println("[READING]::"+i);
                System.out.println(log.get(i));
            }
            lastread=size;
        }

        pb.startCommand();
    }
}
