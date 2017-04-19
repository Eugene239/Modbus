package ru.epavlov.spring.rest;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eugene on 21.03.2017.
 */
@org.springframework.web.bind.annotation.RestController
public class SerialRest {
    private final int MAX_CNT= 50;
    private final int CLEAR = 40;
    private  SerialPort serialPort;
    private ArrayList<String> monitor = new ArrayList<>();
    @PostMapping("/serial")
    public Response serial(@RequestParam(value = "action",defaultValue = "read") String action,
                           @RequestParam(value = "port",defaultValue = "")String port,
                           @RequestParam(value = "baudRate",defaultValue = "9600")String baudRate){
        switch (action){
            case "connect":if (ports().contains(port)) connect(port,baudRate);  return Response.ok().build();
            case "disconnect": if (serialPort!=null && serialPort.isOpened()) disconnect(); return Response.ok().build();
            case "read":  return  Response.ok().entity(monitor).build();
            case "ports": return Response.ok().entity(ports()).build();
            case "status": return Response.ok().entity(serialPort!=null && serialPort.isOpened()).build();
        }
        return Response.noContent().build();
    }

    private ArrayList<String> ports(){
        ArrayList<String> list = new ArrayList<>(Arrays.asList(SerialPortList.getPortNames()));
        return list;
    }
    private void disconnect(){
        monitor.clear();
        try {
          //  serialPort.removeEventListener();
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("[DISCONNECTED MONITOR]");
    }
    private void connect(String port,String baudRate){
        monitor.clear();
        if (serialPort!=null && serialPort.isOpened()) {
            try {
             //   serialPort.removeEventListener();
                        serialPort.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        serialPort = new SerialPort(port);

        try {

            if (serialPort.isOpened()) serialPort.closePort();
            serialPort.openPort();
            serialPort.setParams(Integer.parseInt(baudRate),8,1,0);
          //  readPort();
            new Thread(()->{
                String s="";
               while (serialPort.isOpened()){
                   try {
                       if (serialPort.getInputBufferBytesCount()>0){
                          char c= (char) serialPort.readBytes(1)[0];
                           if (c=='\n') {
                               if (!s.equals("\n"))monitor.add(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())).toString()+": "+s);
                               s="";
                           }
                           else s+=c;
                       }
                   } catch (SerialPortException e) {
                       e.printStackTrace();
                   }
               }
            }).start();


        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("[CONNECTED MONITOR]" + serialPort.getPortName()+":"+baudRate);



    }

    private void clear(){
        int cnt = 0;
        while (cnt<CLEAR){
            cnt++;
            monitor.remove(0);
        }
    }
}
