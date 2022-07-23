package co.com.sofka.model.card.gateways;

import co.com.sofka.model.card.Card;
import reactor.core.publisher.Flux;

public interface CardRepository {
    Flux<Card> findAll();
}
