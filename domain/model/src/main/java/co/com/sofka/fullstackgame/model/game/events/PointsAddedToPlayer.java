package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;
import co.com.sofka.fullstackgame.model.game.values.Points;

import java.awt.*;

public class PointsAddedToPlayer extends DomainEvent {

    private final PlayerId playerId;
    private final Points points;

    public PointsAddedToPlayer(PlayerId playerId, Points points) {
        super("game.PointsAddedToPlayer");
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
