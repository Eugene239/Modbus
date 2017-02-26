package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.ModbusException;

import java.util.HashMap;

/**
 * Created by Eugene on 19.02.2017.
 */
public interface ModbusConnection {
   //  boolean connectOld();
     void connect(ConnectionParameters connection) throws Exception;
     void disconnect();
     void readCoilsList(int offset, int size);
     void readHregList(int offset, int size);
     HashMap<Integer,Boolean> getCoilMap();
     HashMap<Integer,Integer> getHregMap();
     boolean isConnected();
     void writeCoil(int id, boolean value) throws ModbusException;
     void writeHreg(int id, int value) throws  ModbusException;

}
