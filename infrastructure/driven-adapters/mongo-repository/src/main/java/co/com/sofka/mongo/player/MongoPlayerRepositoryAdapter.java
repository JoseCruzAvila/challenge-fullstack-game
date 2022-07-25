package co.com.sofka.mongo.player;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.player.Player;
import co.com.sofka.model.player.gateways.PlayerRepository;
import co.com.sofka.mongo.helper.AdapterOperations;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
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
                .flatMap(currentPlayer -> this.findById("_id", currentPlayer.getId()));
    }

    @Override
    public Flux<Player> findAll() {
        var lookup = this.playerLookup();
        var project = this.playerProjection();
        var aggregation = Aggregation.newAggregation(lookup, project);

        return mongoTemplate.aggregate(aggregation, COLLECTION, String.class)
                .map(this::toEntityFromJson);
    }

    public Mono<Player> findById(String criteria, String id) {
        var lookup = this.playerLookup();
        var project = this.playerProjection();
        var match = Aggregation.match(Criteria.where(criteria).is(id));
        var aggregation = Aggregation.newAggregation(lookup, project, match);

        return mongoTemplate.aggregate(aggregation, COLLECTION, String.class)
                .map(this::toEntityFromJson)
                .single();
    }

    @Override
    protected PlayerDocument toData(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setName(player.getName());
        playerDocument.setEmail(player.getEmail());
        playerDocument.setPoints(player.getPoints());
        playerDocument.setCards(player.getDeck().stream()
                .map(card -> new ObjectId(card.getId()))
                .collect(Collectors.toSet()));

        return playerDocument;
    }

    private Player toEntityFromJson(String body) {
        var jsonPlayer = new JSONObject(body);
        var deck = jsonPlayer.getJSONArray("deck")
                .toList()
                .stream()
                .map(JSONObject::new)
                .map(card -> new Gson().fromJson(card.toString(), Card.class))
                .collect(Collectors.toSet());

        Player player = new Player();
        player.setId(jsonPlayer.getJSONObject("_id").getString("$oid"));
        player.setName(jsonPlayer.getString("name"));
        player.setEmail(jsonPlayer.getString("email"));
        player.setPoints(jsonPlayer.getDouble("points"));
        player.setDeck(deck);

        return player;
    }

    private LookupOperation playerLookup() {
        return LookupOperation.newLookup()
                .from("cardDocument")
                .localField("cards")
                .foreignField("_id")
                .as("deck");
    }

    private ProjectionOperation playerProjection() {
        return Aggregation.project("_id", "name", "email", "points", "deck");
    }
}
