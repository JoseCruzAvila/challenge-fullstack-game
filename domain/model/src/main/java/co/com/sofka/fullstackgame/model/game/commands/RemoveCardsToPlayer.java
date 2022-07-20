package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class RemoveCardsToPlayer extends DomainEvent {

    private final PlayerId playerId;
    private final CardId cardId;

    public RemoveCardsToPlayer(PlayerId playerId, CardId cardId) {
        super("game.RemoveCardsToPlayer");
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
