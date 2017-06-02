package com.wise2c.samples.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public interface WsLifecycleListener {

    void onOpen(Session userSession);

    void onClose(Session userSession, CloseReason reason);

    void onError(Throwable e);

}
