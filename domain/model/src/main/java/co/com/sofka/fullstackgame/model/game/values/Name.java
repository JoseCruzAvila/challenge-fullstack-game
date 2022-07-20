package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.Objects;

public class Name implements ValueObject<String> {

    private final String name;

    public Name(String name) {
        this.name = Objects.requireNonNull(name);
        if (this.name.isBlank()){
            throw new IllegalArgumentException("EL nombre no puede estar vacio");
        }
    }

    @Override
    public String value() {
        return name;
    }

    public static Name newName(String name){
        return new Name(name);
    }
}
