package com.wise2c.samples.entity;

public class Stack extends Resource {

    private String name;

    private String state;

    private String dockerCompose;

    private String rancherCompose;

    private boolean startOnCreate;

    public Stack() {
        super("stack");
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

    public String getDockerCompose() {
        return dockerCompose;
    }

    public void setDockerCompose(String dockerCompose) {
        this.dockerCompose = dockerCompose;
    }

    public String getRancherCompose() {
        return rancherCompose;
    }

    public void setRancherCompose(String rancherCompose) {
        this.rancherCompose = rancherCompose;
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
