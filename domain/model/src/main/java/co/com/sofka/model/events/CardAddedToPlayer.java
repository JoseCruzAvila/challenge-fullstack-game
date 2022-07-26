package co.com.sofka.model.events;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.model.player.Player;

public class CardAddedToPlayer extends DomainEvent<Player> {

    public CardAddedToPlayer(Player player) {
        super("player.CardAdded", player);
    }
}
