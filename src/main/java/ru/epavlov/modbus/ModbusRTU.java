package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import jssc.SerialPortList;

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
    private SerialParameters parameters;
    private ModbusSerialMaster modbus;
    private HashMap<Integer,Integer> hregMap = new HashMap<>();
    private HashMap<Integer,Boolean>  coilMap = new HashMap<>();
    private boolean connected=false;
    private Register register;
    public ModbusRTU(String portname) {
        init(portname, encoding, baudRate, databits, stopBits, parity);
    }

    public ModbusRTU(String port, String encoding, int baudRate, int databits, int stopBits, int parity) {
        this.port = port;
        this.encoding = encoding;
        this.baudRate = baudRate;
        this.databits = databits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    private void init(String port, String encoding, int baudRate, int databits, int stopBits, int parity) {
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
        parameters.setEncoding(encoding);
        modbus = new ModbusSerialMaster(parameters);
    }

    public static void main(String[] args) {

        String portName = SerialPortList.getPortNames()[0];
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
            while (true) {
                //BitVector bitVector = modbusSerialMaster.readCoils(0, 10);
                Register[] register = modbusSerialMaster.readMultipleRegisters(0, 40);
                for (int i = 0; i < 40; i++) {
                    System.out.print(register[i] + ", ");
                }
                System.out.println();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  ConnectionParameters serialConnection = new ConnectionParameters(new SerialParameters());
    }
//
//    public boolean connectOld() {
//        try {
//            //modbus.setRetries(4);
//            modbus.connect();
//            System.out.println("ModbusRTU::connected to "+ port);
//            connected = true;
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public void connect(ConnectionParameters connection) throws Exception {
        parameters.setPortName(connection.getSerialPort());
        parameters.setDatabits(connection.getDatabits());
        parameters.setParity(connection.getParity());
        parameters.setEncoding("rtu");
        parameters.setStopbits(connection.getStopBits());
        parameters.setEcho(false);
        parameters.setBaudRate(connection.getBaudRate());
        if (connected) modbus.disconnect();
        modbus = new ModbusSerialMaster(parameters);
        modbus.connect();
        connected = true;
    }

    public void disconnect() {
        connected=false;
        modbus.disconnect();

    }

    public void readCoilsList(int offset, int size) {
      //  ArrayList<Coil> list = new ArrayList<Coil>();

        try {
            BitVector bitVector = modbus.readCoils(offset, size);
            for (int i = 0; i < bitVector.size(); i++) {
                coilMap.put(offset + i, bitVector.getBit(i));
              //  coilArrayList.add(new Coil(offset + i, bitVector.getBit(i)));
            }
        } catch (ModbusException e) {
            e.printStackTrace();
        }
       // return list;
    }

    public void readHregList(int offset, int size) {

      //  ArrayList<Hreg> list = new ArrayList<>();
        try {
            Register[] registers = modbus.readMultipleRegisters(offset, size);
            if (register==null) register= registers[0];
            for (int i = 0; i < registers.length; i++) {
                hregMap.put(offset+i,registers[i].getValue());
            }
        } catch (ModbusException e) {
            e.printStackTrace();
        }
     //   return list;
    }

    @Override
    public HashMap<Integer, Integer> getHregMap() {
        return hregMap;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public HashMap<Integer, Boolean> getCoilMap() {
        return coilMap;
    }
    public void  writeCoil(int id, boolean value) throws ModbusException {
            modbus.writeCoil(id,value);
    }
    public void  writeHreg(int id, int value) throws ModbusException {
        //Register register = new Register()
        register.setValue(value);
        modbus.writeSingleRegister(id,register);
    }




}
