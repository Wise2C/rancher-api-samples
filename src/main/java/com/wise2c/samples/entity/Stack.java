package com.wise2c.samples.entity;

public class Stack extends Resource {

    private String name;

    private String state;

    public Stack() {
        super("stack");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Stack{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}
