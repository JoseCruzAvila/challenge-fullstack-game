package co.com.sofka.fullstackgame.handler;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class AppSocketHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(session.receive()
                .map(msg -> "RECEIVED ON SERVER :: " + msg.getPayloadAsText())
                .map(session::textMessage));
    }
}
