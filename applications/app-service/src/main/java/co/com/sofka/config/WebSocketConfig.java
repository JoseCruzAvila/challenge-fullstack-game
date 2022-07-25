package co.com.sofka.config;

import co.com.sofka.bus.RabbitMQConsumer;
import co.com.sofka.websocket.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebSocketConfig  {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Bean
    public Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public HandlerMapping webSocketMapping(final SocketHandler socketHandler) {
        Map<String, WebSocketHandler> route = new HashMap<>();
        route.put("/retrieve/{gameId}", socketHandler);

        SimpleUrlHandlerMapping handler = new SimpleUrlHandlerMapping();
        handler.setOrder(Ordered.HIGHEST_PRECEDENCE);
        handler.setUrlMap(route);

        return handler;
    }

    @Bean
    public HandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
