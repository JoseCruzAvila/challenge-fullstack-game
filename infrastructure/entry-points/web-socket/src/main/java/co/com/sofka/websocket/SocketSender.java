package co.com.sofka.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class SocketSender {
    private final WebSocketClient client = new ReactorNettyWebSocketClient();


    public void sendMessage(String socketUri, String message) {
        client.execute(URI.create("ws://".concat(socketUri)),
                session -> session.send(Mono.just(message)
                                .map(session::textMessage)))
                .block();
    }
}
