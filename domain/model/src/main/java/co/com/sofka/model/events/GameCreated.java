package co.com.sofka.model.events;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.game.Game;

public class GameCreated extends DomainEvent<Game> {
    public GameCreated(String aggregateId, Game source) {
        super("game.GameCreated", aggregateId, source);
    }
}
