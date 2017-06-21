package com.wise2c.samples.entity;

public class LoadBalancerService extends Service {

    private LbConfig lbConfig;

    public LoadBalancerService() {
        super("loadBalancerService");
    }

    public LbConfig getLbConfig() {
        return lbConfig;
    }

    public void setLbConfig(LbConfig lbConfig) {
        this.lbConfig = lbConfig;
    }
}
