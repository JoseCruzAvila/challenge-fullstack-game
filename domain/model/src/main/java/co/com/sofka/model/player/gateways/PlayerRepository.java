package co.com.sofka.model.player.gateways;

import co.com.sofka.model.player.Player;
import reactor.core.publisher.Mono;

public interface PlayerRepository {
    Mono<Player> save(Player player);

    Mono<Player> findById(String criteria, String toFind);
}
