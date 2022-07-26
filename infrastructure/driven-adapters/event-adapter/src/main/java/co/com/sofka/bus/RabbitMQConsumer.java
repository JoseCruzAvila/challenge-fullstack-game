package co.com.sofka.bus;

import co.com.sofka.event.EventListenerSubscriber;
import co.com.sofka.generic.events.DomainEvent;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RabbitMQConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final EventListenerSubscriber subscriber;
    private final Flux<DomainEvent> events;

    public RabbitMQConsumer(EventListenerSubscriber subscriber, Flux<DomainEvent> events) {
        this.subscriber = subscriber;
        this.events = events;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "game.handler", durable = "true"),
            exchange = @Exchange(value = "fullstack.game", type = "fanout"),
            key = "game.#"
    ))
    public void gameMessageReceived(Message<String> message) {
        messageShared(message);
    }

    private void messageShared(Message<String> message) {
        log.info("Message received: {}", message.getPayload());
        events.filter(event -> message.getPayload()
                        .contains(event.getType()))
                .map(event -> new Gson().fromJson(message.getPayload(), event.getClass()))
                .subscribe(subscriber::onNext);
    }
}
