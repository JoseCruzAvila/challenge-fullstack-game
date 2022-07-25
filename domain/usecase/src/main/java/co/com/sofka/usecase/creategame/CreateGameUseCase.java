package co.com.sofka.usecase.creategame;

import co.com.sofka.generic.usecase.UseCase;
import co.com.sofka.model.game.Game;
import co.com.sofka.model.game.gateways.GameRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateGameUseCase extends UseCase {
    private final GameRepository repository;

    public Mono<Game> createGame(Game game) {
        return repository.save(game);
    }
}
