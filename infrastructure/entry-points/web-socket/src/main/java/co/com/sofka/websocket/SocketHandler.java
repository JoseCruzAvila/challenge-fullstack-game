package co.com.sofka.websocket;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketHandler implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);
    private static Map<String, Map<String, WebSocketSession>> sessions;

    public SocketHandler() {
        if (Objects.isNull(sessions))
            sessions = new ConcurrentHashMap<>();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String gameId = gameId(session);
        onOpen(gameId, session);

        if (!gameId.equals("retrieve") && sessions.containsKey(gameId)) {
            //return iterateAllSessions(gameId, "");
        }

        //return session.close(new CloseStatus(1016, "Disconnected By: ".concat(gameId)));
        return Mono.empty();
    }

    private void onOpen(String gameId, WebSocketSession session) {
        if (session.isOpen()) {
            var map = sessions.getOrDefault(gameId, new HashMap<>());
            map.put(session.getId(), session);
            sessions.put(gameId, map);
            log.info("Connected by: {}", gameId);
        }
    }

    private String gameId(WebSocketSession session) {
        return session.getHandshakeInfo()
                .getUri()
                .getPath()
                .substring(session.getHandshakeInfo()
                        .getUri()
                        .getPath()
                        .lastIndexOf("/") + 1);
    }

    public void sendMessage(JSONObject event) {
        var gameId = event.getString("gameId");
        var message = new Gson().toJson(event);

        iterateAllSessions(gameId, message)
                .then();
    }

    private Mono<Void> iterateAllSessions(String gameId, String message) {
        return sessions.get(gameId).values()
                .stream()
                .map(session -> this.sendMessages(session, message))
                .findFirst()
                .orElseThrow();
    }

    private Mono<Void> sendMessages(WebSocketSession socketSession, String message) {
        Flux<WebSocketMessage> messages = message.equals("") ? socketSession.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(socketSession::textMessage) : this.sendMessageFromString(socketSession, message);

        return socketSession.send(messages);
    }

    private Flux<WebSocketMessage> sendMessageFromString (WebSocketSession socketSession, String message) {
        return socketSession.receive()
                .flatMap(socketMessage -> Mono.just(message))
                .map(socketSession::textMessage);
    }
}
