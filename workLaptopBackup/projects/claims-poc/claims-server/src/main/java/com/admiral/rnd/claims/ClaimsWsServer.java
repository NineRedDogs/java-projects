package com.admiral.rnd.claims;

import java.util.function.Predicate;

import org.reactivestreams.Publisher;

import com.admiral.rnd.claims.messaging.CaseProducerSingleton;
import com.admiral.rnd.claims.messaging.CaseProducerThread;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;

@ServerWebSocket("/claims/{caseId}")
public class ClaimsWsServer {
    private WebSocketBroadcaster broadcaster;
    private final String id;
    
    public ClaimsWsServer(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
        this.id = Integer.toHexString(System.identityHashCode(this));
    }
    
    private void display(final String msg) {
        System.out.println(id + ":" + msg);
    }

    @OnOpen
    public Publisher<String> onOpen(String caseId, WebSocketSession session) {
        String msg = "case [" + caseId + "] opened !";
        display("onOpen(): " + msg);
        return broadcaster.broadcast(msg, isValid(caseId));
    }

    @OnMessage
    public Publisher<String> onMessage(
            String caseId,
            String caseEvent,
            WebSocketSession session) {
        StringBuilder sb = new StringBuilder("case [" + caseId + "] ");
        sb.append("event [" + caseEvent + "] ");
        sb.append("(session: " + session.getId() + ")");
        display("onMessage(): " + sb.toString());
        
        // add event to topic
        CaseProducerSingleton.INSTANCE.postEvent(caseId, caseEvent);
        
        // alternative - post to topic using a short lived consumer thread
        //CaseProducerThread.postEvent(caseId, caseEvent);
        
        return broadcaster.broadcast(sb.toString(), isValid(caseId));
    }

    @OnClose
    public Publisher<String> onClose(
            String caseId,
            WebSocketSession session) {
        String msg = "case [" + caseId + "] closed !";
        display("onClose(): " + msg);
        return broadcaster.broadcast(msg, isValid(caseId));
    }

    private Predicate<WebSocketSession> isValid(String caseId) {
        return s -> caseId.equalsIgnoreCase(s.getUriVariables().get("caseId", String.class, null));
    }
}
