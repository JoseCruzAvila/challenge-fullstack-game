package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.Identity;

public class CardId extends Identity {

    public CardId(String id) {
        super(id);
    }

    public CardId of (String id){
        return new CardId(id);
    }
}
