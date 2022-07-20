package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.ValueObject;

import java.awt.*;
import java.util.Objects;

public class Points implements ValueObject<Integer> {

    private final Integer points;

    public Points(Integer points) {
        this.points = Objects.requireNonNull(points);
    }


    @Override
    public Integer value() {
        return points;
    }

    public static Points newPoints(int points){
        return new Points(points);
    }
}
