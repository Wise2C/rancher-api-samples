package com.wise2c.samples.websocket;

import org.apache.tomcat.websocket.pojo.PojoEndpointClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;


@ClientEndpoint
public class WebSocketClient implements WsLifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClient.class);
    private final URI endpointURI;
    private Session userSession;
    private MessageHandler messageHandler;
    private Set<WsLifecycleListener> lifecycleListeners = new HashSet<>();
    private final WebSocketContainer container;
    private final ClientEndpointConfig endpointConfig;
    private final Endpoint ep;

    /**
     * @param endpointURI:   websocket endpoint ws:// wws://
     * @param customHeaders: custom http header example, Authorized:xxxxx
     */
    public WebSocketClient(URI endpointURI, Map<String, List<String>> customHeaders) {
        try {
            this.container = ContainerProvider.getWebSocketContainer();
            this.ep = new PojoEndpointClient(this, Collections.emptyList());
            ClientConfig clientConfig = new ClientConfig(customHeaders);
            ClientEndpointConfig.Builder builder = ClientEndpointConfig.Builder.create();
            builder.configurator(clientConfig);
            this.endpointConfig = builder.build();
            this.endpointURI = endpointURI;
            container.connectToServer(ep, endpointConfig, this.endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(endpointURI.toString(), e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        for (WsLifecycleListener listener : lifecycleListeners) {
            listener.onOpen(userSession);
        }
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
        LOGGER.info("websocket connection close");
        for (WsLifecycleListener listener : lifecycleListeners) {
            listener.onClose(userSession, reason);
        }
        reConnection();
    }


    @Override
    @OnError
    public void onError(Throwable e) {
        LOGGER.error("websocket connection error" + e.getMessage(), e);
        for (WsLifecycleListener listener : lifecycleListeners) {
            listener.onError(e);
        }
    }


    @OnMessage
    public void onBinary(byte[] data, Session session) {
        if (this.messageHandler != null) {
            this.messageHandler.handleBinary(data, session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (this.messageHandler != null) {
            this.messageHandler.handleText(message, session);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void addLifecycleListener(WsLifecycleListener listener) {
        this.lifecycleListeners.add(listener);
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendText(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public void sendBinary(ByteBuffer bytes) {
        try {
            this.userSession.getBasicRemote().sendBinary(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session getUserSession() {
        return userSession;
    }

    private void reConnection() {
        try {
            LOGGER.info("websocket re-connection close");
            container.connectToServer(ep, endpointConfig, this.endpointURI);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
