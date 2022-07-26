package co.com.sofka.usecase.playerbyemail;

import co.com.sofka.generic.usecase.UseCase;
import co.com.sofka.model.player.Player;
import co.com.sofka.model.player.gateways.PlayerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PlayerByEmailUseCase extends UseCase {
    private final PlayerRepository repository;

    public Mono<Player> findByEmail(String email) {
        return repository.findById("email", email);
    }
}
