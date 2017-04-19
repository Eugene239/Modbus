package ru.epavlov.spring.rest;

import jssc.SerialPortList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epavlov.entity.Coil;
import ru.epavlov.entity.Hreg;
import ru.epavlov.modbus.ConnectionParameters;
import ru.epavlov.modbus.ModbusConnection;
import ru.epavlov.modbus.ModbusRTU;
import ru.epavlov.modbus.ModbusTCP;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;


@org.springframework.web.bind.annotation.RestController
public class RestController {
    protected static ConnectionParameters connectionParameters;
    protected static ModbusConnection modbus = new ModbusRTU();
    @PostMapping("/parameters")
    private Response parameters(
            @RequestParam(value = "modbusId", defaultValue = "1") int id,
            @RequestParam(value = "type", defaultValue = "RTU") String type,
            @RequestParam(value = "action", defaultValue = "get") String action,
            @RequestParam(value = "portName", defaultValue = "") String portName,
            @RequestParam(value = "baudRate", defaultValue = "9600") int baudRate,
            @RequestParam(value = "echo", defaultValue = "false") String echoS,
            @RequestParam(value = "parity", defaultValue = "0") int parity,
            @RequestParam(value = "dataBits", defaultValue = "8") int dataBits,
            @RequestParam(value = "stopBits", defaultValue = "1") int stopBits,
            @RequestParam(value = "ipPort", defaultValue = "502") int ipPort,
            @RequestParam(value = "ip", defaultValue = "") String ip) {

        if (action.equals("get")) {
            //возвращаем подключение
            return Response.ok().entity(connectionParameters).build();
        }
        try {
            if (type.equals("RTU") && action.equals("create") && ports().contains(portName)) {
                    boolean echo = Boolean.parseBoolean(echoS);
                    connectionParameters = new ConnectionParameters(id,portName, baudRate, dataBits, stopBits, parity, echo);
                    return Response.ok().entity(connectionParameters).build();
            }
            if (type.equals("TCP") && !ip.equals("")) {
                connectionParameters = new ConnectionParameters(id, ip,ipPort);
                return Response.ok().entity(connectionParameters).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @PostMapping("/connect")
    private Response connect(@RequestParam(value = "action", defaultValue = "status") String action) {
        if (action.equals("status")) {
            return Response.ok().entity(modbus.isConnected()).build();
        }
        if (action.equals("disconnect")) {
            if (modbus.isConnected()) modbus.disconnect();
            System.out.println("[DISCONNECTED]");
            return Response.ok().entity(modbus.isConnected()).build();
        }
        if (action.equals("connect")) {
            try {
                if (modbus.isConnected()) modbus.disconnect();
                switch (connectionParameters.getType()){
                    case TCP: modbus= new ModbusTCP(); break;
                    case RTU: modbus = new ModbusRTU(); break;
                }
                modbus.connect(connectionParameters);
                System.out.println("[CONNECTED TO " + connectionParameters.getType() + "]");
                return Response.ok().entity(modbus.isConnected()).build();
            } catch (Exception e) {
                modbus.disconnect();
                e.printStackTrace();
                return Response.noContent().build();
            }
        }
        return Response.noContent().build();
    }
   // private String port = "COM8";
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};





    @PostMapping("/coils")
    public ArrayList<Coil> getCoils(
            @RequestParam(value = "offset_coil", defaultValue = "0") String offset,
            @RequestParam(value = "size", defaultValue = "0") String size, HttpServletRequest request) {
        ArrayList<Coil> list = new ArrayList<>();
        //System.out.println("/coils :: offset_coil: "+offset+"  size: "+size);
        int off = Integer.parseInt(offset);
        int sz = Integer.parseInt(size);
        if ( modbus.isConnected()&& sz>0 && sz<=500) {
            modbus.getCoilMap(off, sz).forEach((i,b)->list.add(new Coil(i,b)));
        }
        return list;
    }

    @PostMapping("/hregs")
    public Response getHregs(@RequestParam(value = "offset_coil", defaultValue = "0") String offset,
                                    @RequestParam(value = "size", defaultValue = "0") String size, HttpServletRequest request) {
        ArrayList<Hreg> list = new ArrayList<>();
         System.out.println("/hregs::    offset_coil:" + offset + "   size:" + size);
        if (modbus.isConnected()) {
            modbus.getHregMap(Integer.parseInt(offset),Integer.parseInt(size)).forEach((i1,i2)->{
                list.add(new Hreg(i1,i2));
            });
            return Response.ok().entity(list).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @PostMapping("/setCoil")
    public Response setCoil(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "value") String value) {
        System.out.println("/setCoil::" + id + ":" + value);
        try {
            if (!modbus.isConnected()) throw new Exception("NO CONNECTION");
            int _id = Integer.parseInt(id);
            boolean _value = Boolean.parseBoolean(value);
            modbus.writeCoil(_id, _value);
            return Response.status(200).build();
        } catch (Exception e) {
            System.err.println(e.toString());
            try {
                modbus.connect(connectionParameters);// попытка переподключиться
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return Response.status(400).entity(e.toString()).build();
        }
    }

    @PostMapping("/setHreg")
    public Response setHreg(@RequestParam(value = "id") String id,
                            @RequestParam(value = "value") String value) {
        try {
            if (!modbus.isConnected()) throw new Exception("NO CONNECTION");
            int _id = Integer.parseInt(id);
            int _value = Integer.parseInt(value);
            System.out.println("/setHreg::" + id + ":" + value);
            modbus.writeHreg(_id, _value);
            return Response.status(200).build();
        } catch (Exception e) {
            System.err.println(e.toString());
            try {
                modbus.connect(connectionParameters); // попытка переподключиться
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return Response.status(400).entity(e.toString()).build();
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @PostMapping("/port")
    private Response port(@RequestParam(value = "reg", defaultValue = "list") String reg) {
        if (reg.equals("list")){
            ArrayList<String> list = new ArrayList<>(Arrays.asList(SerialPortList.getPortNames()));
            return Response.status(Response.Status.OK).entity(list.stream().filter(s -> s.contains("COM")||s.contains("ACM")||s.contains("USB")).toArray()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private ArrayList<String> ports(){
        return  new ArrayList<>(Arrays.asList(SerialPortList.getPortNames()));
    }


}
