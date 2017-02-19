package ru.epavlov.entity;

/**
 * Created by Eugene on 19.02.2017.
 */
public class Coil {
    private boolean value;
    private int id;

    public Coil(int id, boolean value) {
        this.value = value;
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}
