package com.wise2c.samples.action;

public class ServiceRestart {

    private RollingRestartStrategy rollingRestartStrategy;

    public RollingRestartStrategy getRollingRestartStrategy() {
        return rollingRestartStrategy;
    }

    public void setRollingRestartStrategy(RollingRestartStrategy rollingRestartStrategy) {
        this.rollingRestartStrategy = rollingRestartStrategy;
    }
}
