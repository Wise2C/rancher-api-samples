package com.wise2c.samples.websocket;

import javax.websocket.ClientEndpointConfig;
import java.util.List;
import java.util.Map;

public class ClientConfig extends ClientEndpointConfig.Configurator {

    private Map<String, List<String>> customHeaders;

    public ClientConfig(Map<String, List<String>> customHeaders) {
        this.customHeaders = customHeaders;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        this.customHeaders.keySet().forEach(key -> {
            headers.put(key, customHeaders.get(key));
        });
        super.beforeRequest(headers);
    }
}
