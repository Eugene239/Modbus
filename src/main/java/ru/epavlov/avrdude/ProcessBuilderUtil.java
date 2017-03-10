package ru.epavlov.avrdude;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eugene on 07.03.2017.
 */
public class ProcessBuilderUtil {
    private void startCommand() throws IOException {
        //ProcessBuilder pb = new ProcessBuilder("ipconfig");
        Process p = Runtime.getRuntime().exec("ipconfig");
        BufferedReader output =  new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s;
        while((s=output.readLine())!=null){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws IOException {
       // ProcessBuilderUtil pb = new ProcessBuilderUtil();
       // pb.startCommand();
        JsonObject jsonObject =new JsonObject();
        //JsonObject jsonSemaphore = new JsonObject();
        JsonArray array = new JsonArray();
        for(int i=0;i<4;i++){
            JsonObject object = new JsonObject();
            object.addProperty(i+"",false);
            array.add(object);
        }
        jsonObject.add("semaphore",array);

        //JsonObject jsonSvetofor = new JsonObject();
        array = new JsonArray();
        for(int i=0;i<8;i++){
            JsonObject object = new JsonObject();
            object.addProperty(i+"",false);
            array.add(object);
        }
        jsonObject.add("strelka",array);

        array = new JsonArray();
        for(int i=0;i<30;i++){
            JsonObject object = new JsonObject();
            object.addProperty(i+"",false);
            array.add(object);
        }

        //JsonObject jsonStrelka = new JsonObject();

      //  jsonObject.add("semaphore",jsonSemaphore);
        jsonObject.add("svetofor",array);
       // jsonObject.add("svetofor",jsonSvetofor);
        System.out.println(jsonObject.toString());
    }
}
