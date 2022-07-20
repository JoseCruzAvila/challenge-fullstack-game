package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.Objects;

public class Characteristics implements ValueObject<String> {

    private final String characteristics;

    public Characteristics(String characteristics) {
        this.characteristics = Objects.requireNonNull(characteristics);
    }


    @Override
    public String value() {
        return characteristics;
    }

    public static Characteristics newCharacteristics(String characteristics){
        return new Characteristics(characteristics);
    }
}
