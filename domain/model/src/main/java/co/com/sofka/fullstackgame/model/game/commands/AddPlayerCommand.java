package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class AddPlayerCommand extends DomainEvent {

    private final PlayerId playerId;

    public AddPlayerCommand(PlayerId playerId) {
        super("game.AddPlayerCommand");
        this.playerId = playerId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }
}
