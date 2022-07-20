package co.com.sofka.fullstackgame.model.game;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.events.*;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.GameId;
import co.com.sofka.fullstackgame.model.game.values.PlayerId;
import co.com.sofka.fullstackgame.model.game.values.Points;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Game extends AggregateEvent<GameId> {

    protected GameId gameId;
    protected boolean playing;
    protected PlayerId winner;
    protected Set<PlayerId> players;

    public Game(GameId gameId, PlayerId playerId) {
        super(gameId);
        appendChange(new GameCreated(gameId,playerId)).apply();
    }

    private Game(GameId gameId){
        super(gameId);
        subscribe(new GameChange(this));
    }

    public static Game from(GameId gameId, List<DomainEvent> events){
        var game = new Game(gameId);
        events.forEach(game::applyEvent);
        return game;
    }

    public void addPlayer(PlayerId playerId){
        appendChange(new PlayerAdded(playerId)).apply();
    }

    public void addPointsToPlayer(PlayerId playerId, Points points){
        appendChange(new PointsAddedToPlayer(playerId,points)).apply();
    }

    public void addCardsToPlayer(PlayerId playerId,CardId cardId){
        appendChange(new CardsAddedToPlayer(playerId, cardId)).apply();
    }

    public void removeCardsToPlayer(PlayerId playerId,CardId cardId){
        appendChange(new CardsRemovedToPLayer(playerId,cardId)).apply();
    }

    public void asignWinner(PlayerId playerId){
        appendChange(new WinnerAsigned(playerId)).apply();
    }

    public Optional<PlayerId> getPlayerById(PlayerId playerId){
        return players.stream()
                .filter(player -> player.equals(playerId))
                .findFirst();
    }

    public boolean isPlaying() {

        return playing;
    }

    public PlayerId winner() {
        return winner;
    }

    public Set<PlayerId> players() {
        return players;
    }
}
