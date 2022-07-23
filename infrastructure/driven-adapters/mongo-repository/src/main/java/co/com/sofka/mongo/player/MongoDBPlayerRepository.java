package co.com.sofka.mongo.player;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBPlayerRepository extends ReactiveMongoRepository<PlayerDocument, String>, ReactiveQueryByExampleExecutor<PlayerDocument> {
}
