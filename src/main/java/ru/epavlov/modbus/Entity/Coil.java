package ru.epavlov.modbus.Entity;

/**
 * Created by Eugene on 19.02.2017.
 */
public class Coil {
    private int id;
    private boolean value;

    public Coil(int id, boolean value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
