package ru.epavlov.entity;

/**
 * Created by Eugene on 14.03.2017.
 */
public class Desc_Entity {
    private int id;
    private String text;

    public Desc_Entity(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
