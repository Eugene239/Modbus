package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import java.util.HashMap;

/**
 * Created by Eugene on 13.02.2017.
 */
public class ModbusRTU implements ModbusConnection {
    private String port;
    private String encoding = "rtu";
    private int baudRate = 9600;
    private int databits = 8;
    private int stopBits = 1;
    private int parity = 0;
    private int modbusId = 1;
    private SerialParameters parameters = new SerialParameters();
    private ModbusSerialMaster modbus;
    private boolean connected = false;

    //  private Register register;
//    public ModbusRTU(String portname) {
//        init(portname, encoding, baudRate, databits, stopBits, parity);
//    }

//    public ModbusRTU(String port, String encoding, int baudRate, int databits, int stopBits, int parity) {
//        this.port = port;
//        this.encoding = encoding;
//        this.baudRate = baudRate;
//        this.databits = databits;
//        this.stopBits = stopBits;
//        this.parity = parity;
//    }
//
//    private void init(String port, String encoding, int baudRate, int databits, int stopBits, int parity) {
//        this.port = port;
//        this.encoding = encoding;
//        this.baudRate = baudRate;
//        this.databits = databits;
//        this.stopBits = stopBits;
//        this.parity = parity;
//        parameters = new SerialParameters();
//        parameters.setPortName(port);
//        parameters.setEcho(false);
//        parameters.setParity(parity);
//        parameters.setBaudRate(baudRate);
//        parameters.setDatabits(databits);
//        parameters.setStopbits(stopBits);
//        parameters.setEncoding(encoding);
//        modbus = new ModbusSerialMaster(parameters);
//    }


    @Override
    public void connect(ConnectionParameters connection) throws Exception {
        parameters.setPortName(connection.getSerialPort());
        parameters.setDatabits(connection.getDataBits());
        parameters.setParity(connection.getParity());
        parameters.setEncoding("rtu");
        parameters.setStopbits(connection.getStopBits());
        parameters.setEcho(connection.isEcho());
        parameters.setBaudRate(connection.getBaudRate());
        modbusId = connection.getModbusId();
        if (connected) modbus.disconnect();
        modbus = new ModbusSerialMaster(parameters);
        modbus.connect();
        modbus.readCoils(modbusId, 0,10);
        connected = true;
    }

    public void disconnect() {
        connected = false;
        if (modbus!=null) modbus.disconnect();

    }

    @Override
    public HashMap<Integer, Boolean> getCoilMap(int offset, int size) {
        HashMap<Integer, Boolean> map = new HashMap<>();
        try {
            BitVector bitVector = modbus.readCoils(modbusId, offset, size);
            for (int i = 0; i < bitVector.size(); i++) {
                map.put(offset + i, bitVector.getBit(i));
            }
        } catch (ModbusException e) {
            e.printStackTrace();
           // return  map;
           // disconnect();
        }

        return map;
    }

    @Override
    public HashMap<Integer, Integer> getHregMap(int offset, int size) {
        HashMap<Integer, Integer> map = new HashMap<>();
        try{
            Register[] registers = modbus.readMultipleRegisters(modbusId,offset, size);
            for (int i = 0; i < registers.length; i++) {
                map.put(offset + i, registers[i].getValue());
            }
        }catch (ModbusException e){
          //  disconnect();
          //  connected=false;
           e.printStackTrace();
        }
        return map;

    }

    @Override
    public boolean isConnected() {
        return connected;
    }


    public void writeCoil(int id, boolean value) throws ModbusException {
        modbus.writeCoil(modbusId, id, value);
    }

    public void writeHreg(int id, int value) throws ModbusException {
        try {
            Register register = modbus.readMultipleRegisters(modbusId, id, 1)[0];
            register.setValue(value);
            modbus.writeSingleRegister(modbusId, id, register);
        } catch (ModbusException e) {
            e.printStackTrace();
        }
    }


}
