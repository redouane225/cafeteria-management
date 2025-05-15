package com.example.cafeteriamanagement.model;
public class Table {
    private int id;
    private int number;

    public Table(int id, int number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }
}

