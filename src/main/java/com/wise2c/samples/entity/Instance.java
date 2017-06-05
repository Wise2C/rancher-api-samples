package com.wise2c.samples.entity;

import java.util.List;
import java.util.Map;

public class Instance extends Resource {

    private List<String> capAdd;
    private List<String> command;
    private List<String> dataVolumes;
    private List<String> dnsSearch;
    private String imageUuid;
    private Map<String, String> environment;
    private List<String> ports;
    private List<String> userPorts;

    public Instance() {
        super("instance");
    }

    public List<String> getUserPorts() {
        return userPorts;
    }

    public void setUserPorts(List<String> userPorts) {
        this.userPorts = userPorts;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public String getImageUuid() {
        return imageUuid;
    }

    public void setImageUuid(String imageUuid) {
        this.imageUuid = imageUuid;
    }

    public List<String> getDnsSearch() {
        return dnsSearch;
    }

    public void setDnsSearch(List<String> dnsSearch) {
        this.dnsSearch = dnsSearch;
    }

    public List<String> getCapAdd() {
        return capAdd;
    }

    public void setCapAdd(List<String> capAdd) {
        this.capAdd = capAdd;
    }

    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    public List<String> getDataVolumes() {
        return dataVolumes;
    }

    public void setDataVolumes(List<String> dataVolumes) {
        this.dataVolumes = dataVolumes;
    }


    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", ports=" + ports +
                ", imageUuid='" + imageUuid + '\'' +
                ", environment=" + environment +
                ", command=" + command +
                '}';
    }
}
