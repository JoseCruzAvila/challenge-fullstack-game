package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class CardsRemovedToPLayer extends DomainEvent {
    private final PlayerId playerId;
    private final CardId cardId;

    public CardsRemovedToPLayer(PlayerId playerId, CardId cardId) {
        super("game.CardsRemovedToPLayer");
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
