package co.com.sofka.api.game;

import co.com.sofka.event.EventPublisher;
import co.com.sofka.model.card.Card;
import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.game.Game;
import co.com.sofka.model.player.Player;
import co.com.sofka.usecase.creategame.CreateGameUseCase;
import co.com.sofka.usecase.startgame.StartGameUseCase;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameHandler {
    @Autowired
    private final EventPublisher<DomainEvent> publisher;
    private final CreateGameUseCase createGameUseCase;
    private final StartGameUseCase startGameUseCase;

    public Mono<ServerResponse> listenPOSTCreateGameUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(String.class)
                .map(this::establishGame)
                .flatMap(createGameUseCase::createGame)
                .flatMap(game -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(game)));
    }

    public Mono<ServerResponse> listenPOSTStartGameUseCase(ServerRequest serverRequest) {
        String gameId = serverRequest.pathVariable("gameId");
        var request = startGameUseCase.gameById(gameId)
                .flatMap(startGameUseCase::startGame);

        request.subscribe(publisher::publish);

        return request.flatMap(game -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(game)));
    }

    private Game establishGame(String body) {
        var jsonBody = new JSONObject(body);
        var deck = jsonBody.getJSONObject("player")
                .getJSONArray("deck")
                .toList()
                .stream()
                .map(currentCard -> new Gson().fromJson(currentCard.toString(), Card.class))
                .collect(Collectors.toSet());

        JSONObject jsonPlayer = jsonBody.getJSONObject("player");
        Player player = new Player();
        player.setId(jsonPlayer.getString("id"));
        player.setName(jsonPlayer.getString("name"));
        player.setEmail(jsonPlayer.getString("email"));
        player.setPoints(jsonPlayer.getDouble("points"));
        player.setDeck(deck);

        return new Game(jsonBody.getString("id"), player);
    }
}
