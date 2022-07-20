package co.com.sofka.fullstackgame.controller;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.model.game.commands.CreateGameCommand;
import co.com.sofka.fullstackgame.usecase.game.CreateGameUseCase;
import co.com.sofka.infraestructure.asyn.SubscriberEvent;
import co.com.sofka.infraestructure.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/game")
public class GameController {
    @Autowired
    private SubscriberEvent subscriberEvent;
    @Autowired
    private EventStoreRepository eventStoreRepository;
    @Autowired
    private CreateGameUseCase createGameUseCase;

    @PostMapping
    public String createGame(@RequestBody CreateGameCommand command) {
        createGameUseCase.addRepository(domainEventRepository());
        UseCaseHandler.getInstance()
                .setIdentifyExecutor(command.getGameId())
                .asyncExecutor(createGameUseCase, new RequestCommand<>(command))
                .subscribe(subscriberEvent);

        return "ready";
    }

    private DomainEventRepository domainEventRepository() {
        return new DomainEventRepository() {
            @Override
            public List<DomainEvent> getEventsBy(String aggregateId) {
                return eventStoreRepository.getEventsBy("game", aggregateId);
            }

            @Override
            public List<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
                return eventStoreRepository.getEventsBy(aggregateName, aggregateRootId);
            }
        };
    }
}
