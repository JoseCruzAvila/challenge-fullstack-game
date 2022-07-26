package co.com.sofka.event;

import co.com.sofka.bus.RabbitMQConsumer;
import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.generic.usecase.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Component
public class EventListenerSubscriber {
    private static final Logger log = LoggerFactory.getLogger(EventListenerSubscriber.class);
    private final Flux<UseCase.UseCaseWrap> useCases;
    private final EventPublisher<DomainEvent> publisher;

    @Autowired
    public EventListenerSubscriber(Flux<UseCase.UseCaseWrap> useCases, EventPublisher<DomainEvent> publisher) {
        this.useCases = useCases;
        this.publisher = publisher;
    }

    public void onNext(DomainEvent domainEvent) {
        var event = Objects.requireNonNull(domainEvent);
        var currentUseCase = this.useCases.blockFirst();
        log.info("data: {}", currentUseCase.eventType());
        log.info("data 2: {}", event.getType());
        this.useCases.filter(useCaseWrap -> useCaseWrap.eventType()
                .equals(event.getType()))
                .map(UseCase.UseCaseWrap::useCase)
                .flatMap(useCase -> useCase.execute(event.getSource()))
                .subscribe(lastEventGenerated -> publisher.publish((DomainEvent) lastEventGenerated));
    }
}
