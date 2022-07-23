package co.com.sofka.fullstackgame.usecase.game;

import co.com.sofka.business.generic.UseCase;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.business.support.ResponseEvents;
import co.com.sofka.fullstackgame.model.game.Game;
import co.com.sofka.fullstackgame.model.game.commands.CreateGameCommand;
import co.com.sofka.fullstackgame.model.game.values.GameId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;
import org.springframework.stereotype.Component;

@Component
public class CreateGameUseCase extends UseCase<RequestCommand<CreateGameCommand>, ResponseEvents> {
    @Override
    public void executeUseCase(RequestCommand<CreateGameCommand> requestCommand) {
        var command = requestCommand.getCommand();
        var game = new Game(GameId.of(command.getGameId()), PlayerId.of(command.getPlayerId()));

        emit().onResponse(new ResponseEvents(game.getUncommittedChanges()));
    }
}
