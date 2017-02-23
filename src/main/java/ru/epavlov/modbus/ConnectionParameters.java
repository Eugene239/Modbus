package ru.epavlov.modbus;

import com.ghgande.j2mod.modbus.util.SerialParameters;

/**
 * Created by Eugene on 23.02.2017.
 */
public class ConnectionParameters {
     public enum TypeEnum{
        RTU,TCP
    }
    private SerialParameters parameters = new SerialParameters();
    private TypeEnum type = TypeEnum.RTU;
    private String ip;
    private int ipPort;

    public ConnectionParameters() {
    }

    public ConnectionParameters(String serialPort, int baudRate, int databits, int stopBits, int parity, boolean echo) {
        type = TypeEnum.RTU;
        parameters.setEcho(echo);
        parameters.setEncoding("rtu");
        parameters.setStopbits(stopBits);
        parameters.setDatabits(databits);
        parameters.setPortName(serialPort);
        parameters.setBaudRate(baudRate);
        parameters.setParity(parity);
    }

    public TypeEnum getType() {
        return type;
    }

    public String getSerialPort() {
        return parameters.getPortName();
    }

    public int getBaudRate() {
        return parameters.getBaudRate();
    }

    public int getDatabits() {
        return parameters.getDatabits();
    }

    public int getStopBits() {
        return parameters.getStopbits();
    }

    public int getParity() {
        return parameters.getParity();
    }

    public String getIp() {
        return ip;
    }

    public int getIpPort() {
        return ipPort;
    }

    protected SerialParameters getParameters() {
        return parameters;
    }
}
