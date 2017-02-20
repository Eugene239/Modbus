package ru.epavlov.entity;

/**
 * Created by Eugene on 20.02.2017.
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

    public int getValue() {
        return value;
    }
}
