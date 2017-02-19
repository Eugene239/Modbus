package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import jssc.SerialPortList;

import java.util.ArrayList;

/**
 * Created by Eugene on 13.02.2017.
 */
public class ModbusRTU implements ModbusConnection{
    private String port;
    private String encoding = "rtu";
    private int baudRate = 9600;
    private int databits = 8;
    private int stopBits = 1;
    private int parity=0;
    private SerialParameters parameters;
    private ModbusSerialMaster modbus;

    public ModbusRTU(String portname){
        init(portname,encoding,baudRate,databits,stopBits,parity);
    }

    public ModbusRTU(String port, String encoding, int baudRate, int databits, int stopBits, int parity) {
        this.port = port;
        this.encoding = encoding;
        this.baudRate = baudRate;
        this.databits = databits;
        this.stopBits = stopBits;
        this.parity = parity;
    }
    private void init(String port, String encoding, int baudRate, int databits, int stopBits, int parity){
        this.port = port;
        this.encoding = encoding;
        this.baudRate = baudRate;
        this.databits = databits;
        this.stopBits = stopBits;
        this.parity = parity;
        parameters = new SerialParameters();
        parameters.setPortName(port);
        parameters.setEcho(false);
        parameters.setParity(parity);
        parameters.setBaudRate(baudRate);
        parameters.setDatabits(databits);
        parameters.setStopbits(stopBits);
        modbus = new ModbusSerialMaster(parameters);
    }

    public static void main(String[] args) {

       String portName= SerialPortList.getPortNames()[0];
        SerialParameters serialParameters = new SerialParameters();
        serialParameters.setPortName(portName);
        serialParameters.setParity(0);

        serialParameters.setEncoding("rtu");
        serialParameters.setEcho(false);
        serialParameters.setBaudRate(9600);
        serialParameters.setDatabits(8);
        serialParameters.setStopbits(1);
        ModbusSerialMaster modbusSerialMaster = new ModbusSerialMaster(serialParameters);
        try {
            modbusSerialMaster.connect();
           while(true) {
               //BitVector bitVector = modbusSerialMaster.readCoils(0, 10);
               Register[] register= modbusSerialMaster.readMultipleRegisters(0,40);
               for (int i = 0; i < 40; i++) {
                   System.out.print(register[i] + ", ");
               }
               System.out.println();
               Thread.sleep(500);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  SerialConnection serialConnection = new SerialConnection(new SerialParameters());
    }
    public boolean connect() {
        try{
            modbus.setRetries(4);
            modbus.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void disconnect() {
        modbus.disconnect();
    }

    public ArrayList<Boolean> getCoils(int offset, int size) {
        try {
            BitVector bitVector = modbus.readCoils(offset,size);
            for (int i= 0; i <bitVector.size();i++){
             //   bitVector.g
            }
        } catch (ModbusException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Integer> getHreg(int offset, int size) {
        return null;
    }


}
