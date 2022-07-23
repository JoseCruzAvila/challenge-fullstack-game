package co.com.sofka.mongo.game;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.game.Game;
import co.com.sofka.model.game.gateways.GameRepository;
import co.com.sofka.model.player.Player;
import co.com.sofka.mongo.helper.AdapterOperations;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class MongoGameRepositoryAdapter extends AdapterOperations<Game, GameDocument, String, MongoDBGameRepository> implements GameRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    private static final String COLLECTION = "gameDocument";

    public MongoGameRepositoryAdapter(MongoDBGameRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Game.class));

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Game> save(Game game) {
        return Mono.just(game)
                .map(this::toData)
                .flatMap(this::saveData)
                .flatMap(currentGame -> this.findById(currentGame.getId()));
    }

    @Override
    public Flux<Game> findAll() {
        var lookup = LookupOperation.newLookup()
                .from("playerDocument")
                .localField("playersList")
                .foreignField("_id")
                .as("players");
        var project = Aggregation.project("_id", "gameId", "playing", "winnerId", "players");
        var playersUnwind = Aggregation.unwind("players");
        var playerLookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("players.cards")
                .foreignField("_id")
                .as("players.deck");
        var group = Aggregation.group("_id", "gameId", "playing", "winnerId")
                .push("players")
                .as("players");
        var aggregation = Aggregation.newAggregation(
                lookup, project, playersUnwind, playerLookup, group
        );

        return mongoTemplate.aggregate(aggregation, COLLECTION, String.class)
                .map(this::toEntityFromJson);
    }

    @Override
    public Mono<Game> findById(String id) {
        var lookup = LookupOperation.newLookup()
                .from("playerDocument")
                .localField("playersList")
                .foreignField("_id")
                .as("players");
        var project = Aggregation.project("_id", "gameId", "playing", "winnerId", "players");
        var match = Aggregation.match(Criteria.where("_id").is(id));
        var playersUnwind = UnwindOperation.newUnwind()
                .path("players")
                .noArrayIndex()
                .preserveNullAndEmptyArrays();
        var playerLookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("players.cards")
                .foreignField("_id")
                .as("players.deck");
        var group = Aggregation.group("_id", "gameId", "playing", "winnerId")
                .push("players")
                .as("players");
        var aggregation = Aggregation.newAggregation(
                lookup, project, match, playersUnwind, playerLookup, group);

        return mongoTemplate.aggregate(aggregation, COLLECTION, String.class)
                .map(this::toEntityFromJson)
                .single();
    }

    public Mono<Game> findByGameId(String gameId) {
        var lookup = LookupOperation.newLookup()
                .from("playerDocument")
                .localField("playersList")
                .foreignField("_id")
                .as("players");
        var project = Aggregation.project("_id", "gameId", "playing", "winnerId", "players");
        var match = Aggregation.match(Criteria.where("gameId").is(gameId));
        var playersUnwind = UnwindOperation.newUnwind()
                .path("players")
                .noArrayIndex()
                .preserveNullAndEmptyArrays();
        var playerLookup = LookupOperation.newLookup()
                .from("cardDocument")
                .localField("players.cards")
                .foreignField("_id")
                .as("players.deck");
        var group = Aggregation.group("_id", "gameId", "playing", "winnerId")
                .push("players")
                .as("players");
        var aggregation = Aggregation.newAggregation(
                lookup, project, match, playersUnwind, playerLookup, group
        );

        return mongoTemplate.aggregate(aggregation, COLLECTION, String.class)
                .map(this::toEntityFromJson)
                .single();
    }

    @Override
    protected GameDocument toData(Game game) {
        GameDocument gameDocument = new GameDocument();
        gameDocument.setId(game.getId());
        gameDocument.setGameId(game.getGameId());
        gameDocument.setPlaying(game.getPlaying());
        gameDocument.setWinnerId(game.getWinner() != null ? new ObjectId(game.getWinner().getId()) : null);
        gameDocument.setPlayersList(game.getPlayers().stream()
                .map(player -> new ObjectId(player.getId()))
                .collect(Collectors.toSet()));

        return gameDocument;
    }

    @Override
    protected Game toEntity(GameDocument gameDocument) {
        return this.findById(gameDocument.getId())
                .block();
    }

    private Game toEntityFromJson (String body) {
        var jsonBody = new JSONObject(body);
        var jsonData = jsonBody.getJSONObject("_id");

        var players = jsonBody.getJSONArray("players")
                .toList()
                .stream()
                .map(player -> new JSONObject(new Gson().toJson(player)))
                .map(this::playerFromJson)
                .collect(Collectors.toSet());

        var winner = players.stream()
                .filter(this.verifyIfWinner(jsonData))
                .findFirst()
                .orElse(null);

        Game game = new Game();
        game.setId(jsonData.getJSONObject("_id").getString("$oid"));
        game.setGameId(jsonData.getString("gameId"));
        game.setPlaying(jsonData.getBoolean("playing"));
        game.setWinner(winner);
        game.setPlayers(players);

        return game;
    }

    private Player playerFromJson(JSONObject jsonPlayer) {
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

    private Predicate<Player> verifyIfWinner(JSONObject jsonData) {
        return player -> jsonData.has("winnerId") && player.getId()
                .equals(jsonData.getJSONObject("winnerId")
                        .getString("$oid"));
    }
}
