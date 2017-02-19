package ru.epavlov.modbus;

import java.util.ArrayList;

/**
 * Created by Eugene on 19.02.2017.
 */
public interface ModbusConnection {
    public boolean connect();
    public void disconnect();
    public ArrayList<Boolean> getCoils(int offset, int size);
    public ArrayList<Integer> getHreg(int offset, int size);
}
