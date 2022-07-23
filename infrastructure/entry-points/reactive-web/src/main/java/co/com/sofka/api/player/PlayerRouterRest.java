package co.com.sofka.api.player;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class PlayerRouterRest {

    @Bean
    public RouterFunction<ServerResponse> playerRouterFunction(PlayerHandler playerHandler) {
        return route(POST("/player"), playerHandler::listenPOSTCreatePlayerUseCase);
    }
}
