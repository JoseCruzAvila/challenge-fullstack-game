package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;
import co.com.sofka.fullstackgame.model.game.values.Points;

public class AddPointsToPlayer extends DomainEvent {

    private final PlayerId playerId;
    private final Points points;

    public AddPointsToPlayer(PlayerId playerId, Points points) {
        super("game.AddPointsToPlayer");
        this.playerId = playerId;
        this.points = points;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public Points getPoints() {
        return points;
    }
}
