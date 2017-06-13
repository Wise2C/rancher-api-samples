package com.wise2c.samples;

import com.wise2c.samples.entity.Environment;
import com.wise2c.samples.websocket.MessageHandler;
import com.wise2c.samples.websocket.WebSocketClient;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RancherWebSocketClient implements MessageHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(RancherWebSocketClient.class);

    private final String endpoint;
    private final String accesskey;
    private final String secretKey;

    public RancherWebSocketClient(String endpoint, String accesskey, String secretKey) {
        this.endpoint = endpoint;

        this.accesskey = accesskey;
        this.secretKey = secretKey;
    }

    /**
     * ws://rancher-server/v2-beta/projects/1a5558/subscribe?eventNames=resource.change
     */
    public void connection(Environment environment) throws URISyntaxException {

        Map<String, List<String>> headers = new HashMap<>();
        String auth = accesskey + ":" + secretKey;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.put("Authorization", Collections.singletonList(authHeader));

        String wsEndpoint = endpoint.replace("http://", "ws://") + "/projects/" + environment.getId();

        String subscribeUrl = wsEndpoint.endsWith("/")
                ? String.format("%ssubscribe?eventNames=resource.change", wsEndpoint)
                : String.format("%s/subscribe?eventNames=resource.change", wsEndpoint);

        WebSocketClient socketClient = new WebSocketClient(new URI(subscribeUrl), headers);
        socketClient.addMessageHandler(this);


    }

    @Override
    public void handleText(String message, Session session) {
        LOGGER.info(message);
    }

    @Override
    public void handleBinary(byte[] data, Session session) {

    }
}
