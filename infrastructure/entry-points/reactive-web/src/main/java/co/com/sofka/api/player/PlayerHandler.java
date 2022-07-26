package co.com.sofka.api.player;

import co.com.sofka.model.player.Player;
import co.com.sofka.usecase.createplayer.CreatePlayerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayerHandler {
    private final CreatePlayerUseCase createPlayerUseCase;

    public Mono<ServerResponse> listenPOSTCreatePlayerUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Player.class)
                .flatMap(createPlayerUseCase::createPlayer)
                .flatMap(player -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(player)));
    }
}
