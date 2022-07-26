package co.com.sofka.model.events;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.player.Player;

public class PlayerCreated extends DomainEvent<Player> {
    public PlayerCreated(String aggregateId, Player source) {
        super("player.PlayerCreated", aggregateId, source);
    }
}
