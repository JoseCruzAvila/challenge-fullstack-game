package co.com.sofka.fullstackgame.model.game.commands;

import co.com.sofka.domain.generic.Command;

public class CreateGameCommand extends Command {

    private String gameId;
    private String playerId;

    public CreateGameCommand() {}

    public CreateGameCommand(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }
    public String getPlayerId() {
        return playerId;
    }
}
