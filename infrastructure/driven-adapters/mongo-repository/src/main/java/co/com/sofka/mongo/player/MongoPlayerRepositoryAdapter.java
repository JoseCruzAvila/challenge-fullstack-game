package co.com.sofka.mongo.player;

import co.com.sofka.model.player.Player;
import co.com.sofka.model.player.gateways.PlayerRepository;
import co.com.sofka.mongo.card.CardDocument;
import co.com.sofka.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Repository
public class MongoPlayerRepositoryAdapter extends AdapterOperations<Player, PlayerDocument, String, MongoDBPlayerRepository> implements PlayerRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoPlayerRepositoryAdapter(MongoDBPlayerRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Player.class));

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Player> saveAll(Flux<Player> players) {
        return repository.saveAll(players.map(this::toData))
                .map(this::toEntity);
    }

    @Override
    public Flux<Player> findAll() {
        return mongoTemplate.findAll(PlayerDocument.class)
                .map(this::toEntity);
    }

    public Mono<Player> findById(String criteria, String toFind) {
        var condition = Query.query(Criteria.where(criteria).is(toFind));

        return mongoTemplate.find(condition, PlayerDocument.class)
                .map(this::toEntity)
                .single();
    }

    @Override
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
}
