package com.wise2c.samples.entity;

public class Stack extends Resource {

    private String name;

    private String state;

    private String externalId;

    private boolean startOnCreate;

    public Stack() {
        super("stack");
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isStartOnCreate() {
        return startOnCreate;
    }

    public void setStartOnCreate(boolean startOnCreate) {
        this.startOnCreate = startOnCreate;
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
