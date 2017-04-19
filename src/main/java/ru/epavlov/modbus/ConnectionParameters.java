package ru.epavlov.modbus;

/**
 * Created by Eugene on 23.02.2017.
 */
public class ConnectionParameters {
     public enum TypeEnum{
        RTU,TCP
    }
   // private SerialParameters parameters = new SerialParameters();
    private TypeEnum type = TypeEnum.RTU;
    private String ip;
    private String serialPort="";
    private int baudRate=9600;
    private int dataBits=8;
    private int stopBits=1;
    private int parity =0;
    private boolean echo=false;
    private int ipPort;
    private int modbusId;
    public ConnectionParameters() {
    }

    public ConnectionParameters(int modbusId, String serialPort, int baudRate, int databits, int stopBits, int parity, boolean echo) {
        this.type = TypeEnum.RTU;
        this.modbusId = modbusId;
        this.serialPort =serialPort;
        this.baudRate=baudRate;
        this.dataBits=databits;
        this.stopBits=stopBits;
        this.parity =parity;
        this.echo=echo;
    }

    public ConnectionParameters(int modbusId, String ip,int port){
        this.type=TypeEnum.TCP;
        this.ip=ip;
        this.ipPort=port;
        this.modbusId=modbusId;
    }
    public TypeEnum getType() {
        return type;
    }


    public String getSerialPort() {
        return serialPort;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public boolean isEcho() {
        return echo;
    }

    public String getIp() {
        return ip;
    }

    public int getIpPort() {
        return ipPort;
    }



    public int getModbusId() {
        return modbusId;
    }
}
