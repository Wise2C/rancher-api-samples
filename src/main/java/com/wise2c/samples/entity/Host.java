package com.wise2c.samples.entity;

import java.util.List;
import java.util.Map;

public class Host extends Resource {

    private String hostname;

    private String agentIpAddress;

    private Map<String, String> labels;

    private List<PublicEndpoint> publicEndpoints;

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

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public List<PublicEndpoint> getPublicEndpoints() {
        return publicEndpoints;
    }

    public void setPublicEndpoints(List<PublicEndpoint> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
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
