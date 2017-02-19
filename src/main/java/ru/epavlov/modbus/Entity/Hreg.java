package ru.epavlov.modbus.Entity;

/**
 * Created by Eugene on 19.02.2017.
 */
public class Hreg {
    private int id;
    private int value;

    public Hreg(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
