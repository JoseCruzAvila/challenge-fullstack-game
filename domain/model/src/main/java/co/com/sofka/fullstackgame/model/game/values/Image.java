package co.com.sofka.fullstackgame.model.game.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.Objects;

public class Image implements ValueObject<String> {

    public final String url;

    public Image(String url) {
        this.url = Objects.requireNonNull(url);
    }

    @Override
    public String value() {
        return url;
    }

    public static Image newImageUrl(String url){
        return new Image(url);
    }

}
