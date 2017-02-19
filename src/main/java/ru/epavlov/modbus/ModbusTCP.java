package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;

/**
 * Created by Eugene on 18.02.2017.
 */
public class ModbusTCP {
    public static void main(String[] args) {

        ModbusTCPMaster modbusTCPMaster = new ModbusTCPMaster("192.168.1.44",502);
    //    modbusTCPMaster.
        try{
            modbusTCPMaster.setReconnecting(true);
            modbusTCPMaster.setRetries(4);
             modbusTCPMaster.connect();

            while (true) {

                Register[] register = modbusTCPMaster.readMultipleRegisters(0, 40);
                for (int i = 0; i < 40; i++) {
                    System.out.print(register[i] + ", ");
                }
                System.out.println();
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
