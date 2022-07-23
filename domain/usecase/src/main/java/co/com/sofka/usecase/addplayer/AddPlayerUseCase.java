package co.com.sofka.usecase.addplayer;

import co.com.sofka.model.game.Game;
import co.com.sofka.model.game.gateways.GameRepository;
import co.com.sofka.model.player.Player;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddPlayerUseCase {
    private final GameRepository repository;

    public Mono<Game> addPlayer(String gameId, Player player) {
        var gameToUpdate = repository.findById(gameId);
        return gameToUpdate.map(game -> {
            game.addPlayer(player);
            return game;
        });
    }
}
