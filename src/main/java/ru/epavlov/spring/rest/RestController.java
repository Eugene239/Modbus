package ru.epavlov.spring.rest;

import jssc.SerialPortList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epavlov.entity.Coil;
import ru.epavlov.entity.Hreg;
import ru.epavlov.modbus.ModbusRTU;
import ru.epavlov.modbus.SerialConnection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;



@org.springframework.web.bind.annotation.RestController
public class RestController {
    private SerialConnection connection= new SerialConnection();
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


    private ModbusRTU modbusRTU = new ModbusRTU(getPorts("")[0]);
    {
        modbusRTU.connect();
    }
    //@RequestMapping("/")
    @PostMapping("/getPorts")
    public String[] getPorts(@RequestParam(value = "name") String name){
        return SerialPortList.getPortNames();

      //  return "HELLO "+name;
    }
    @PostMapping("/coils")
    public ArrayList<Coil> getCoils(
            //  @RequestParam(value = "register") String register,
            @RequestParam(value = "offset",defaultValue = "0") String offset,
            @RequestParam(value = "size",defaultValue = "0") String size, HttpServletRequest request) {

        if(!modbusRTU.isConnected()){
            modbusRTU.connect();
        }
        System.out.println("/coils");
        ArrayList<Coil> list = new ArrayList<>();
        modbusRTU.readCoilsList(Integer.parseInt(offset),Integer.parseInt(size));
        modbusRTU.getCoilMap().forEach((i,b)->{
            list.add(new Coil(i,b));
       //     System.out.print(i+";"+b+" ");
        });
       // modbusRTU.getCoilMap().e
     //   System.out.println();
      //  System.out.println(getClientIpAddress(request));
        return list;
    }
    @PostMapping("/hregs")
    public ArrayList<Hreg> getHregs(@RequestParam(value = "offset",defaultValue = "0") String offset,
                                    @RequestParam(value = "size",defaultValue = "0") String size, HttpServletRequest request){

        if(!modbusRTU.isConnected()){
            modbusRTU.connect();
        }
        System.out.println("/hregs::    offset:"+offset+"   size:"+size);
        ArrayList<Hreg> list = new ArrayList<>();
        modbusRTU.readHregList(Integer.parseInt(offset),Integer.parseInt(size));
        modbusRTU.getHregMap().forEach((i,i1)->{
            list.add(new Hreg(i,i1));
            //     System.out.print(i+";"+b+" ");
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
            modbusRTU.writeCoil(_id,_value);
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
            modbusRTU.writeHreg(_id,_value);
            return Response.status(200).build();
        }catch (Exception e){
            System.err.println(e.toString());
            return Response.status(400).build();
        }
    }
    private  String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
    @PostMapping("/port")
    private Response port(@RequestParam(value = "reg") String reg){
        if (reg.equals("list")) return Response.status(Response.Status.OK).entity(SerialPortList.getPortNames()).build();
        if  (reg.equals("connection")) return Response.status(Response.Status.OK).entity(port).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @PostMapping("/connection")
    private Response connection(
                                @RequestParam(value = "type")String type,
                                @RequestParam(value = "action")String action,
                                @RequestParam(value = "portName")String portName,
                                @RequestParam(value = "baudRate",defaultValue = "9600")String baudRate,
                                @RequestParam(value = "echo",defaultValue = "false")boolean echo,
                                @RequestParam(value = "parity", defaultValue = "0")String parity,
                                @RequestParam(value = "dataBits",defaultValue = "8")String dataBits,
                                @RequestParam(value = "stopBits",defaultValue = "1")String stopBits){

        try {
            if (type.equals("RTU")) {
                    if (action.equals("create")){

                    }
                    if(action.equals("get")){
                        //возвращаем подключение
                        return Response.ok().entity(connection).build();
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
