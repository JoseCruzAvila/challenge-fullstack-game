package co.com.sofka.model.events;

import co.com.sofka.model.player.Player;
import lombok.*;

import java.util.Set;

@Getter
@Setter
public class GameStarted extends DomainEvent {
    private final String id;
    private final String gameId;
    private final Boolean playing;
    private final Player winner;
    private final Set<Player> players;

    public GameStarted(String id, String gameId, Boolean playing, Player winner, Set<Player> players) {
        super("game.GameStarted");
        this.id = id;
        this.gameId = gameId;
        this.playing = playing;
        this.winner = winner;
        this.players = players;
    }
}
