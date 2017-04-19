package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.BitVector;

import java.util.HashMap;

/**
 * Created by Eugene on 18.02.2017.
 */
public class ModbusTCP  implements ModbusConnection{
    private ModbusTCPMaster modbus=null;
    private boolean connected =false;
    private int modbusId =1;
    @Override
    public void connect(ConnectionParameters connection) throws Exception {
        if (connected) modbus.disconnect();
        modbus = new ModbusTCPMaster(connection.getIp(),connection.getIpPort());
        modbusId = connection.getModbusId();
        modbus.setRetries(4);
        modbus.setReconnecting(true);
        modbus.connect();
        modbus.readCoils(modbusId,0,1);
        connected=true;
    }

    @Override
    public void disconnect() {
        if (modbus!=null) {
            modbus.disconnect();
        }
        connected=false;
    }

    @Override
    public HashMap<Integer, Boolean> getCoilMap(int offset, int size) {
        HashMap<Integer,Boolean> map = new HashMap<>();
        if (modbus!=null&& connected){
            try {
                BitVector vector = modbus.readCoils(modbusId,offset,size);
                for(int i=0;i<vector.size();i++){
                    map.put(i+offset,vector.getBit(i));
                }
            } catch (ModbusException e) {
                disconnect();
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public HashMap<Integer, Integer> getHregMap(int offset, int size) {
        HashMap<Integer,Integer> map = new HashMap<>();
        if (modbus!=null&& connected){
            try {
                Register[] registers = modbus.readMultipleRegisters(modbusId,offset,size);
                for(int i=0;i<registers.length;i++){
                    map.put(i+offset,registers[i].getValue());
                }
            } catch (ModbusException e) {
                disconnect();
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void writeCoil(int id, boolean value) throws ModbusException {
        if (modbus!=null && connected){
            modbus.writeCoil(modbusId,id,value);
        }
    }

    @Override
    public void writeHreg(int id, int value) throws ModbusException {
        if (modbus!=null && connected) {
            Register register = modbus.readMultipleRegisters(modbusId, id, 1)[0];
            register.setValue(value);
            modbus.writeSingleRegister(modbusId, id, register);
        }
    }
}
