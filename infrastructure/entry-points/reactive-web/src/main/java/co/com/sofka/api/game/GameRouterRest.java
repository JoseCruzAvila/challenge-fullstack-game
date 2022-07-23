package co.com.sofka.api.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class GameRouterRest {

    @Bean
    public RouterFunction<ServerResponse> gameRouterFunction(GameHandler gameHandler) {
        return route(POST("/game"), gameHandler::listenPOSTCreateGameUseCase)
                .andRoute(POST("/game/start/{gameId}"), gameHandler::listenPOSTStartGameUseCase);
    }
}
