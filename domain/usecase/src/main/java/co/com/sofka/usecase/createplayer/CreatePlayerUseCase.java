package co.com.sofka.usecase.createplayer;

import co.com.sofka.model.player.Player;
import co.com.sofka.model.player.gateways.PlayerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreatePlayerUseCase {
    private final PlayerRepository repository;

    public Mono<Player> createPlayer(Player player) {
        return repository.save(player);
    }
}
