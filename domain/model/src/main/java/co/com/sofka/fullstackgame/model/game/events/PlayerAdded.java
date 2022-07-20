package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class PlayerAdded extends DomainEvent {

    private final PlayerId playerId;
    public PlayerAdded(PlayerId playerId) {
        super("game.playerAdded");
        this.playerId = playerId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }
}
