package ru.epavlov.modbus;

/**
 * Created by Eugene on 23.02.2017.
 */
public class SerialConnection {
     public enum TypeEnum{
        RTU,TCP
    }
    private TypeEnum type = TypeEnum.RTU;
    private String port;
    private String encoding = "rtu";
    private int baudRate = 9600;
    private int databits = 8;
    private int stopBits = 1;
    private int parity = 0;

    public TypeEnum getType() {
        return type;
    }

    public String getPort() {
        return port;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDatabits() {
        return databits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }
}
