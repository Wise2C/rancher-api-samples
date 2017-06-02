package com.wise2c.samples.websocket;

import org.apache.tomcat.websocket.pojo.PojoEndpointClient;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;


@ClientEndpoint
public class WebSocketClient implements WsLifecycleListener {

    private Session userSession;
    private MessageHandler messageHandler;
    private Set<WsLifecycleListener> lifecycleListeners = new HashSet<>();

    /**
     * @param endpointURI:   websocket endpoint ws:// wws://
     * @param customHeaders: custom http header example, Authorized:xxxxx
     */
    public WebSocketClient(URI endpointURI, Map<String, List<String>> customHeaders) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Endpoint ep = new PojoEndpointClient(this, Collections.emptyList());
            ClientConfig clientConfig = new ClientConfig(customHeaders);
            ClientEndpointConfig.Builder builder = ClientEndpointConfig.Builder.create();
            builder.configurator(clientConfig);

            ClientEndpointConfig endpointConfig = builder.build();

            container.connectToServer(ep, endpointConfig, endpointURI);
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
        for (WsLifecycleListener listener : lifecycleListeners) {
            listener.onClose(userSession, reason);
        }
    }

    @Override
    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
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


}
