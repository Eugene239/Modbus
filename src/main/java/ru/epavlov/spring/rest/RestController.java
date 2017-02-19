package ru.epavlov.spring.rest;


import jssc.SerialPortList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epavlov.entity.Coil;
import ru.epavlov.modbus.ModbusRTU;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;



@org.springframework.web.bind.annotation.RestController
public class RestController {
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
        ArrayList<Coil> list = new ArrayList<>();
        modbusRTU.readCoilsList(Integer.parseInt(offset),Integer.parseInt(size));
        modbusRTU.getCoilMap().forEach((i,b)->{
            list.add(new Coil(i,b));
            System.out.print(i+";"+b+" ");
        });
       // modbusRTU.getCoilMap().e
        System.out.println();
        System.out.println(getClientIpAddress(request));
        return list;
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
}
