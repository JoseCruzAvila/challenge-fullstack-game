package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class CardsAddedToPlayer extends DomainEvent {

    private PlayerId  playerId;
    private CardId cardId;
    public CardsAddedToPlayer(PlayerId playerId, CardId cardId) {
        super("game.CardsAddedToPlayer");
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
