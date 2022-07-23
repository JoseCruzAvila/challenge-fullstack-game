package co.com.sofka.usecase.startgame;

import co.com.sofka.model.events.GameStarted;
import co.com.sofka.model.game.Game;
import co.com.sofka.model.game.gateways.GameRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StartGameUseCase {
    private final GameRepository repository;

    public Mono<Game> gameById(String gameId) {
        return repository.findByGameId(gameId);
    }

    public Mono<GameStarted> startGame(Game game) {
        game.setPlaying(true);
        return repository.save(game)
                .map(newGame -> new GameStarted(
                            newGame.getId(),
                            newGame.getGameId(),
                            newGame.getPlaying(),
                            newGame.getWinner(),
                            newGame.getPlayers()
                    ));
    }
}
