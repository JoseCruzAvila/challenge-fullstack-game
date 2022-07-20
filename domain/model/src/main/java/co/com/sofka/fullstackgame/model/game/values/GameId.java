package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.Identity;

public class GameId extends Identity {

    public GameId(String id) {
        super(id);
    }

    public GameId of (String id){
        return new GameId(id);
    }
}
