package co.com.sofka.model.events;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.game.Game;

public class GameStarted extends DomainEvent<Game> {

    public GameStarted(Game source) {
        super("game.GameStarted", source);
    }
}
