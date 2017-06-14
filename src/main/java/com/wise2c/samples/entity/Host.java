package com.wise2c.samples.entity;

import java.util.List;
import java.util.Map;

public class Host extends Resource {

    private String hostname;

    private String agentIpAddress;

    private Map<String, String> labels;

    private List<PublicEndpoint> publicEndpoints;

    /**
     * memory resource limit
     */
    private int memory;

    /**
     * cpu reousrce limit
     */
    private int milliCpu;

    public Host() {
        super("host");
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getMilliCpu() {
        return milliCpu;
    }

    public void setMilliCpu(int milliCpu) {
        this.milliCpu = milliCpu;
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
                "agentIpAddress='" + agentIpAddress + '\'' +
                ", hostname='" + hostname + '\'' +
                ", labels=" + labels +
                ", publicEndpoints=" + publicEndpoints +
                ", memory=" + memory +
                ", milliCpu=" + milliCpu +
                '}';
    }
}
