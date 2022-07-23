package co.com.sofka.fullstackgame.model.game;

import co.com.sofka.domain.generic.Entity;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.Name;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;
import co.com.sofka.fullstackgame.model.game.values.Points;

import java.awt.*;
import java.util.Set;

public class Player extends Entity<PlayerId> {

    protected PlayerId playerId;
    protected Name name;
    protected Points points;
    protected Set<CardId> cards;

    public Player(PlayerId entityId, Name name, Points points) {
        super(entityId);
        this.name = name;
        this.points = points;
    }

    public void addPoints(Points points){
        this.points = points;
    }

    public void addCards(CardId cardId){
        this.cards.add(cardId);
    }

    public void removeCards(CardId cardId){
        this.cards.remove(cardId);
    }

    public PlayerId id() {
        return playerId;
    }

    public Name name() {
        return name;
    }

    public Points points() {
        return points;
    }
}
