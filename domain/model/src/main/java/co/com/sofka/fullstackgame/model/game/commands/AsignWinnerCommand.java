package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class AsignWinnerCommand extends DomainEvent {

    private final PlayerId playerId;

    public AsignWinnerCommand(PlayerId playerId) {
        super("gamen.AsignWinner");
        this.playerId = playerId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }
}
