package ru.epavlov.spring.rest;

import jssc.SerialPortList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epavlov.entity.Coil;
import ru.epavlov.entity.Hreg;
import ru.epavlov.modbus.ConnectionParameters;
import ru.epavlov.modbus.ModbusConnection;
import ru.epavlov.modbus.ModbusRTU;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;



@org.springframework.web.bind.annotation.RestController
public class RestController {
    private ConnectionParameters connectionParameters;
    private String port = "COM8";
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
            "REMOTE_ADDR" };


    private ModbusConnection modbus = new ModbusRTU("");


    @PostMapping("/coils")
    public ArrayList<Coil> getCoils(
            @RequestParam(value = "offset",defaultValue = "0") String offset,
            @RequestParam(value = "size",defaultValue = "0") String size, HttpServletRequest request) {

        if(!modbus.isConnected()){
            modbus.connectOld();
        }
        System.out.println("/coils");
        ArrayList<Coil> list = new ArrayList<>();
        modbus.readCoilsList(Integer.parseInt(offset),Integer.parseInt(size));
        modbus.getCoilMap().forEach((i, b)->{
            list.add(new Coil(i,b));
        });
        return list;
    }
    @PostMapping("/hregs")
    public ArrayList<Hreg> getHregs(@RequestParam(value = "offset",defaultValue = "0") String offset,
                                    @RequestParam(value = "size",defaultValue = "0") String size, HttpServletRequest request){

        if(!modbus.isConnected()){
            modbus.connectOld();
        }
        System.out.println("/hregs::    offset:"+offset+"   size:"+size);
        ArrayList<Hreg> list = new ArrayList<>();
        modbus.readHregList(Integer.parseInt(offset),Integer.parseInt(size));
        modbus.getHregMap().forEach((i, i1)->{
            list.add(new Hreg(i,i1));
        });
        return list;
    }
    @PostMapping("/setCoil")
    public Response setCoil(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "value") String value){
        System.out.println("/setCoil::"+id+":"+value);
        try{
            int _id= Integer.parseInt(id);
            boolean _value = Boolean.parseBoolean(value);

            modbus.writeCoil(_id,_value);
            return Response.status(200).build();
        }catch (Exception e){
            return Response.status(400).build();
        }
    }
    @PostMapping("/setHreg")
    public Response setHreg(@RequestParam(value = "id") String id,
                            @RequestParam(value = "value") String value){
        try{
            int _id= Integer.parseInt(id);
            int _value = Integer.parseInt(value);
            System.out.println("/setHreg::"+id+":"+value);
            modbus.writeHreg(_id,_value);
            return Response.status(200).build();
        }catch (Exception e){
            System.err.println(e.toString());
            return Response.status(400).build();
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
    private Response port(@RequestParam(value = "reg", defaultValue = "list") String reg){
        if (reg.equals("list")) return Response.status(Response.Status.OK).entity(SerialPortList.getPortNames()).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PostMapping("/connect")
    private Response connect(@RequestParam(value = "action", defaultValue = "status") String action){
        if (action.equals("status")){
            return  Response.ok().entity(modbus.isConnected()).build();
        }
        if (action.equals("disconnect")){
            if (modbus.isConnected()) modbus.disconnect();
            return Response.ok().build();
        }
        if (action.equals("connect")){
            try {
                if(modbus.isConnected()) modbus.disconnect();
                modbus.connect(connectionParameters);
                return Response.ok().entity(connectionParameters).build();
            }catch (Exception e){
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.toString()).build();
            }
        }
        return Response.noContent().build();
    }

    @PostMapping("/parameters")
    private Response parameters(
                                @RequestParam(value = "type")String type,
                                @RequestParam(value = "action")String action,
                                @RequestParam(value = "portName",defaultValue = "COM8")String portName,
                                @RequestParam(value = "baudRate",defaultValue = "9600")int baudRate,
                                @RequestParam(value = "echo",defaultValue = "false")boolean echo,
                                @RequestParam(value = "parity", defaultValue = "0")int parity,
                                @RequestParam(value = "dataBits",defaultValue = "8")int dataBits,
                                @RequestParam(value = "stopBits",defaultValue = "1")int stopBits,
                                @RequestParam(value = "ipPort",defaultValue = "502")String ipPort,
                                @RequestParam(value = "ip",defaultValue = "")String ip
                                ){

        try {
            if (type.equals("RTU")) {
                    if (action.equals("create")){
                        connectionParameters = new ConnectionParameters(portName,baudRate,dataBits,stopBits,parity,echo);
                        return Response.ok().entity(connectionParameters).build();
                    }
                    if(action.equals("get")){
                        //возвращаем подключение
                        return Response.ok().entity(connectionParameters).build();
                    }
            }
            if (type.equals("TCP")) {

            }
        }catch (Exception e){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }
}
