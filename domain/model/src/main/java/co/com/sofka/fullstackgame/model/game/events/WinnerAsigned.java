package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class WinnerAsigned extends DomainEvent {

    private final PlayerId playerId;

    public WinnerAsigned(PlayerId playerId) {
        super("game.WinnerAsigned");
        this.playerId = playerId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }
}
