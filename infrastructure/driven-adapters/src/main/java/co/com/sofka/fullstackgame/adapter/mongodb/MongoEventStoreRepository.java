package co.com.sofka.fullstackgame.adapter.mongodb;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.infraestructure.repository.EventStoreRepository;
import co.com.sofka.infraestructure.store.StoredEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MongoEventStoreRepository implements EventStoreRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoEventStoreRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public List<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
        var query = new Query(Criteria.where("aggregateRootId").is(aggregateRootId));
        var find = reactiveMongoTemplate.find(query, DocumentEventStored.class, aggregateName);
        return find.map(aggregate -> aggregate.getStoredEvent().deserializeEvent())
                .sort(Comparator.comparing(event -> event.when))
                .collectList()
                .block();
    }

    @Override
    public void saveEvent(String aggregateName, String aggregateRootId, StoredEvent storedEvent) {
        var eventStored = new DocumentEventStored();
        eventStored.setAggregateRootId(aggregateRootId);
        eventStored.setStoredEvent(storedEvent);

        reactiveMongoTemplate.save(eventStored, aggregateName);
    }
}
