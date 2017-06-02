package com.wise2c.samples.websocket;

import javax.websocket.Session;
import java.util.Map;

public interface MessageHandler {

    void handleText(String message, Session session);

    void handleBinary(byte[] data, Session session);
}