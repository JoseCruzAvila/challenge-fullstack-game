package co.com.sofka.mongo.card;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.card.gateways.CardRepository;
import co.com.sofka.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MongoCardRepositoryAdapter extends AdapterOperations<Card, CardDocument, String, MongoDBCardRepository>
implements CardRepository
{

    public MongoCardRepositoryAdapter(MongoDBCardRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Card.class));
    }
}
