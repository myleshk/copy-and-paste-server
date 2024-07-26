package hk.myles.CopyAndPaste.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hk.myles.CopyAndPaste.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    Map<String, String> sessionIdUserFragMapping = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        EventType eventType = getMessageType(message);
        String userFrag;
        switch (eventType) {
            case offer, answer -> {
                //OfferAnswerEventMessage offerAnswerData
                OfferAnswerEventMessage offerAnswerEventMessage = parseMessageToEvent(message, OfferAnswerEventMessage.class);
                userFrag = getUserFragFromSdp(offerAnswerEventMessage.getData().getSdp());
            }
            case candidate -> {
                // CandidateEventMessage candidateData
                userFrag = parseMessageToEvent(message, CandidateEventMessage.class).getData().getUsernameFragment();
            }
            default -> throw new IllegalStateException("Unexpected payload event value: " + eventType);
        }

        if (userFrag != null) {
            sessionIdUserFragMapping.put(session.getId(), userFrag);
        }

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    private EventType getMessageType(TextMessage message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.getPayload(), UnknownEventMessage.class).getEvent();
    }

    private <T> T parseMessageToEvent(TextMessage message, Class<T> tClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.getPayload(), tClass);
    }

    private String getUserFragFromSdp(String sdp) {
        Optional<String> optionalPart = Arrays.stream(
                sdp.split("/r/n")).filter(part -> part.startsWith("a=ice-ufrag:")
        ).findFirst();

        return optionalPart.map(s -> s.trim().replaceFirst("a=ice-ufrag:", "")).orElse(null);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        sessionIdUserFragMapping.put(session.getId(), "");
        log.info("Added session. Current sessions: {}", sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        broadcastUserLeft(session);
        sessions.remove(session);
        sessionIdUserFragMapping.remove(session.getId());
        log.info("Removed session. Current sessions: {}", sessions.size());
    }

    private void broadcastUserLeft(WebSocketSession closedSession) throws JsonProcessingException {
        String userFrag = sessionIdUserFragMapping.get(closedSession.getId());
        if (userFrag.isEmpty()) {
            // no need to broadcast
            return;
        }
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String messageJson = objectWriter.writeValueAsString(new Object() {
            public final String event = "userLeft";
            public final Map<String, String> data = Map.of("userFrag", userFrag);
        });

        for (WebSocketSession session : sessions) {
            if (session.isOpen() && !closedSession.getId().equals(session.getId())) {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (IOException e) {
                    log.warn("Error sending user left message to session {}", session.getId());
                }
            }
        }
    }
}