package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;

import java.util.HashMap;

/**
 * Created by Eugene on 19.02.2017.
 */
public interface ModbusConnection {
     void connect(ConnectionParameters connection) throws Exception;
     void disconnect();
     HashMap<Integer,Boolean> getCoilMap(int offset, int size) ;
     HashMap<Integer,Integer> getHregMap(int offset, int size) ;
     boolean isConnected();
     void writeCoil(int id, boolean value) throws ModbusException;
     void writeHreg(int id, int value) throws  ModbusException;

}
