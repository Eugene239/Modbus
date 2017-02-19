package ru.epavlov.spring;

import jssc.SerialPortList;
import ru.epavlov.modbus.ModbusRTU;

/**
 * Created by Eugene on 19.02.2017.
 */
public class Application {
    public static void main(String[] args) throws InterruptedException {
        ModbusRTU modbusRTU = new ModbusRTU(SerialPortList.getPortNames()[0]);
        boolean connected  =modbusRTU.connect();

        while(connected){
           // modbusRTU.readHregList(10,10);

            modbusRTU.readCoilsList(10,10);
            modbusRTU.getCoilMap().forEach((integer, aBoolean) -> {
                System.out.print(integer+":"+aBoolean+" ");
            });
            System.out.println();
            modbusRTU.readHregList(15,10);
            modbusRTU.getHregMap().forEach((i, i2) -> {
                System.out.print(i+":"+i2+" ");
            });
            System.out.println();
            Thread.sleep(100);

        }
    }
}
