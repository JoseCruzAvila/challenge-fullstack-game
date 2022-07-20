package co.com.sofka.fullstackgame.model.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.GameId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class GameCreated extends DomainEvent {

    private final GameId gameId;
    private final PlayerId playerId;

    public GameCreated(GameId gameId, PlayerId playerId) {
        super("game.GameCreated");
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public GameId getGameId() {
        return gameId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }
}
