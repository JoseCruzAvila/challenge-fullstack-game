package co.com.sofka.fullstackgame.model.game;

import co.com.sofka.domain.generic.EventChange;
import co.com.sofka.fullstackgame.model.game.events.GameCreated;
import co.com.sofka.fullstackgame.model.game.events.PlayerAdded;
import co.com.sofka.fullstackgame.model.game.events.PointsAddedToPlayer;
import co.com.sofka.fullstackgame.model.game.events.WinnerAsigned;

import java.util.HashSet;

public class GameChange extends EventChange {
    public GameChange(Game game) {

        apply((GameCreated event) -> {
            game.gameId = event.getGameId();
            game.players = new HashSet<>();
            game.players.add(event.getPlayerId());
        });

        apply((PlayerAdded event) -> {
            game.players.add(event.getPlayerId());
        });

        apply((WinnerAsigned event) -> {
            game.winner = event.getPlayerId();
        });
    }
}
