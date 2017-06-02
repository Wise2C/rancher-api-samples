package com.wise2c.samples.entity;

public class Stack extends Resource {

    private String name;

    public Stack() {
        super("stack");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
