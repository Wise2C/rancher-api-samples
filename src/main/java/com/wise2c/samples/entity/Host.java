package com.wise2c.samples.entity;

public class Host extends Resource {

    private String hostname;

    private String agentIpAddress;

    public Host() {
        super("host");
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAgentIpAddress() {
        return agentIpAddress;
    }

    public void setAgentIpAddress(String agentIpAddress) {
        this.agentIpAddress = agentIpAddress;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id='" + getId() + "'" +
                "agentIpAddress='" + agentIpAddress + '\'' +
                ", hostname='" + hostname + '\'' +
                '}';
    }
}
