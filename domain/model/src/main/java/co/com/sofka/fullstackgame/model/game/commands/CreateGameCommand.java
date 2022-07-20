package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.values.GameId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;

public class CreateGameCommand extends DomainEvent {

    private final GameId gameId;
    private final PlayerId playerId;

    public CreateGameCommand(GameId gameId, PlayerId playerId) {
        super("game.CreateGame");
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
