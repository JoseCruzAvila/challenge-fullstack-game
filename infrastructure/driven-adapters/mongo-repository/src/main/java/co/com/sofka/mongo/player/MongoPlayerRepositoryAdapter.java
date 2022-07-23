package co.com.sofka.mongo.player;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.player.Player;
import co.com.sofka.model.player.gateways.PlayerRepository;
import co.com.sofka.mongo.helper.AdapterOperations;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Repository
public class MongoPlayerRepositoryAdapter extends AdapterOperations<Player, PlayerDocument, String, MongoDBPlayerRepository> implements PlayerRepository {

    private final ReactiveMongoTemplate mongoTemplate;
    private static final String COLLECTION = "playerDocument";

    public MongoPlayerRepositoryAdapter(MongoDBPlayerRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Player.class));

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Player> save(Player player) {
        return Mono.just(player)
                .map(this::toData)
                .flatMap(this::saveData)
                .flatMap(currentPlayer -> this.findById(currentPlayer.getId()));
    }

    @Override
    public Flux<Player> findAll() {
        var lookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("cards")
                .foreignField("_id")
                .as("deck");
        var project = Aggregation.project("_id", "name", "email", "points", "deck");
        var aggregation = Aggregation.newAggregation(lookup, project);

        return mongoTemplate.aggregate(aggregation, COLLECTION, Player.class);
    }

    @Override
    public Mono<Player> findById(String id) {
        var lookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("cards")
                .foreignField("_id")
                .as("deck");
        var project = Aggregation.project("_id", "name", "email", "points", "deck");
        var match = Aggregation.match(Criteria.where("_id").is(id));
        var aggregation = Aggregation.newAggregation(lookup, project, match);

        return mongoTemplate.aggregate(aggregation, COLLECTION, Player.class)
                .single();
    }

    public Mono<Player> findByEmail(String email) {
        var lookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("cards")
                .foreignField("_id")
                .as("deck");
        var project = Aggregation.project("_id", "name", "email", "points", "deck");
        var match = Aggregation.match(Criteria.where("email").is(email));
        var aggregation = Aggregation.newAggregation(lookup, project, match);

        return mongoTemplate.aggregate(aggregation, COLLECTION, Player.class)
                .single();
    }

    @Override
    protected PlayerDocument toData(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setName(player.getName());
        playerDocument.setEmail(player.getEmail());
        playerDocument.setPoints(player.getPoints());
        playerDocument.setCards(player.getDeck().stream()
                .map(card -> new ObjectId(card.getId()).toString())
                .collect(Collectors.toSet()));

        return playerDocument;
    }
}
