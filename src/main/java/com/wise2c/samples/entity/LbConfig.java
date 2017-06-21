package com.wise2c.samples.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LbConfig {

    private String type = "lbConfig";

    private List<PortRule> portRules;

    private StickinessPolicy stickinessPolicy;

    public List<PortRule> getPortRules() {
        return portRules;
    }

    public void setPortRules(List<PortRule> portRules) {
        this.portRules = portRules;
    }

    public StickinessPolicy getStickinessPolicy() {
        return stickinessPolicy;
    }

    public void setStickinessPolicy(StickinessPolicy stickinessPolicy) {
        this.stickinessPolicy = stickinessPolicy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
