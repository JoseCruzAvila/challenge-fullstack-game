package co.com.sofka.fullstackgame.model.game;

import co.com.sofka.domain.generic.Entity;
import co.com.sofka.fullstackgame.model.game.values.CardId;
import co.com.sofka.fullstackgame.model.game.values.Characteristics;
import co.com.sofka.fullstackgame.model.game.values.Image;
import co.com.sofka.fullstackgame.model.game.values.Power;

import java.util.stream.Collector;

public class Card extends Entity<CardId> {

    protected Image image;
    protected Power power;
    protected Characteristics characteristics;

    public Card(CardId entityId, Image image, Power power, Characteristics characteristics) {
        super(entityId);
        this.image = image;
        this.power = power;
        this.characteristics = characteristics;
    }

    public void addImage(Image image){
        this.image = image;
    }

    public void changePower(Power power){
        this.power = power;
    }

    public void addCharacteristic(Characteristics characteristics){
        this.characteristics = characteristics;
    }

    public Image image() {
        return image;
    }

    public Power power() {
        return power;
    }

    public Characteristics characteristics() {
        return characteristics;
    }
}
