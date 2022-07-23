package co.com.sofka.fullstackgame.controller;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.infraestructure.event.EventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/gameData/{gameId}")
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private static Map<String, Map<String, Session>> sessions;

    public WebSocketController() {
        if (Objects.isNull(sessions)) {
            sessions = new ConcurrentHashMap<>();
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId) {
        log.info("Connect by {}", gameId);
        var map = sessions.getOrDefault(gameId, new HashMap<>());
        map.put(session.getId(), session);
        sessions.put(gameId, map);
    }

    @OnClose
    public void onClose(Session session, @PathParam("gameId") String gameId) {
        sessions.get(gameId).remove(session.getId());
        log.info("Desconnect by {}", gameId);

    }

    @OnError
    public void onError(Session session, @PathParam("gameId") String gameId, Throwable throwable) {
        sessions.get(gameId).remove(session.getId());
        log.error(throwable.getMessage(), throwable);

    }

    public void send(String gameId, DomainEvent event) {
        var message = EventSerializer.instance().serialize(event);
        if (Objects.nonNull(gameId) && sessions.containsKey(gameId)) {
            sessions.get(gameId).values()
                    .forEach(session -> session.getAsyncRemote().sendText(message));
        }
    }
}
