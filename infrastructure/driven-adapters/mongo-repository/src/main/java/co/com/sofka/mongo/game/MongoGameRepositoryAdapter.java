package co.com.sofka.mongo.game;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.game.Game;
import co.com.sofka.model.game.gateways.GameRepository;
import co.com.sofka.model.player.Player;
import co.com.sofka.mongo.card.CardDocument;
import co.com.sofka.mongo.helper.AdapterOperations;
import co.com.sofka.mongo.player.PlayerDocument;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Repository
public class MongoGameRepositoryAdapter extends AdapterOperations<Game, GameDocument, String, MongoDBGameRepository> implements GameRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public MongoGameRepositoryAdapter(MongoDBGameRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Game.class));

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Game> findAll() {
        return mongoTemplate.findAll(GameDocument.class)
                .map(this::toEntity);
    }

    public Mono<Game> findById(String criteria, String toFind) {
        var condition = Query.query(Criteria.where(criteria).is(toFind));
        return mongoTemplate.find(condition, GameDocument.class)
                .map(this::toEntity)
                .single();
    }

    @Override
    protected GameDocument toData(Game game) {
        GameDocument gameDocument = new GameDocument();
        gameDocument.setId(game.getId());
        gameDocument.setGameId(game.getGameId());
        gameDocument.setPlaying(game.getPlaying());
        gameDocument.setWinner(game.getWinner() != null ? this.toData(game.getWinner()) : null);
        gameDocument.setPlayers(game.getPlayers().stream()
                .map(this::toData)
                .collect(Collectors.toSet()));

        return gameDocument;
    }

    protected PlayerDocument toData(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setName(player.getName());
        playerDocument.setEmail(player.getEmail());
        playerDocument.setPoints(player.getPoints());
        playerDocument.setDeck(player.getDeck().stream()
                .map(card -> mapper.map(card, CardDocument.class))
                .collect(Collectors.toSet()));

        return playerDocument;
    }

    @Override
    protected Game toEntity(GameDocument gameDocument) {
        Game game = new Game();
        game.setId(gameDocument.getId());
        game.setGameId(gameDocument.getGameId());
        game.setPlaying(gameDocument.getPlaying());
        game.setWinner(gameDocument.getWinner() != null ? this.toEntity(gameDocument.getWinner()) : null);
        game.setPlayers(gameDocument.getPlayers().stream()
                .map(this::toEntity)
                .collect(Collectors.toSet()));

        return game;
    }

    protected Player toEntity(PlayerDocument playerDocument) {
        Player player = new Player();
        player.setName(playerDocument.getName());
        player.setEmail(playerDocument.getEmail());
        player.setPoints(playerDocument.getPoints());
        player.setDeck(playerDocument.getDeck().stream()
                .map(card -> mapper.map(card, Card.class))
                .collect(Collectors.toSet()));

        return player;
    }
}
