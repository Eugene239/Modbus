package ru.epavlov.avrdude;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Eugene on 07.03.2017.
 */
public class ProcessBuilderUtil {
    private ArrayList<String> log = new ArrayList<>();
    public static final String command = "java -version";

    public static final String dir ="C:\\Users\\Eugene\\Desktop\\avrdude\\Modbus\\";
    public static final String modbusHEX1 = "javaModbusTest.ino.hex";
    public static final String modbusHEX2 = "Modbus.ino.hex";
    public static final String command2 = "avrdude -F -patmega2560 -b115200 -PCOM8 -cwiring -D -Uflash:w:"+dir+modbusHEX1+ ":i";
    static Process p;
    static Thread thread;
    static int lastread=0;

    static {

    }

    public void startCommand(String file,String port)  {
        log.clear();
        lastread=0;
        //ProcessBuilder pb = new ProcessBuilder("ipconfig");
        if (thread==null || !thread.isAlive()) {
            thread = new Thread(() -> {
                System.out.println("[PROCESS THREAD]::"+Thread.activeCount());
               // ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c",command);
                ProcessBuilder pb=new ProcessBuilder("avrdude ","-F","-patmega2560","-b115200","-P"+port,"-cwiring","-D","-Uflash:w:"+file+":i");

                pb.redirectErrorStream(true);
                try {
                    p =pb.start(); // Runtime.getRuntime().exec("dir V*");
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
    public void cancel(){
        if (p.isAlive()) p.destroy();
    }
    void readWithIS() throws IOException {
        InputStream is  = p.getInputStream();
        //String s;
        int val;
        String s="";
        while (p.isAlive()) {
            while ((val = is.read()) != -1) {
                if ((char) val != '\n') s += (char) val;
                else {
                    log.add(s);
                    System.out.println(">"+s);
                    s = "";

                }
            }
        }
    }
public boolean inProcess(){
    return !(thread==null || !thread.isAlive());
}
    public  ArrayList<String> getLog() {
        return log;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilderUtil pb = new ProcessBuilderUtil();
      //  pb.startCommand("ipconfig");
        while (thread.isAlive()){
            int size = pb.getLog().size();
            for (int i= lastread;i<size;i++){
                System.out.println(">"+pb.getLog().get(i));
            }
            lastread=size;
        }

//        pb.startCommand();
//        while (thread.isAlive()){
//            int size = pb.getLog().size();
//            for (int i= lastread;i<size;i++){
//                System.out.println(">"+pb.getLog().get(i));
//            }
//            lastread=size;
//        }
    }
}
