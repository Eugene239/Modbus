package ru.epavlov.modbus;

import java.util.HashMap;

/**
 * Created by Eugene on 19.02.2017.
 */
public interface ModbusConnection {
    public boolean connect();
    public void disconnect();
    public void readCoilsList(int offset, int size);
    public void readHregList(int offset, int size);
    public HashMap<Integer,Boolean> getCoilMap();
    public HashMap<Integer,Integer> getHregMap();
    public boolean isConnected();

}
