package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.Player;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class AddCardsToPlayer extends DomainEvent {

    private final PlayerId playerId;
    private final CardId cardId;

    public AddCardsToPlayer(PlayerId playerId, CardId cardId) {
        super("game.AddCardsToPlayer");
        this.playerId = playerId;
        this.cardId = cardId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public CardId getCardId() {
        return cardId;
    }
}
