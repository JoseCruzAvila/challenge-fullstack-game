package co.com.sofka.model.events;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.game.Game;
import lombok.*;

@Getter
@Setter
public class GameStarted extends DomainEvent {
    private final Game source;

    public GameStarted(Game source) {
        super("game.GameStarted");
        this.source = source;
    }
}
