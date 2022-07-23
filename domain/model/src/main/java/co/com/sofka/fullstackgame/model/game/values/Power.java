package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.Objects;

public class Power implements ValueObject<Integer> {

    private final Integer power;

    public Power(Integer power) {
        this.power = Objects.requireNonNull(power);
    }


    @Override
    public Integer value() {
        return power;
    }

    public static Power newPower(Integer power){
        return new Power(power);
    }
}
