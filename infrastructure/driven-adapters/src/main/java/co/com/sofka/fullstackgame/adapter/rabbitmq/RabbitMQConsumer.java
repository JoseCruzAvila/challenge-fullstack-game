package co.com.sofka.fullstackgame.adapter.rabbitmq;

import co.com.sofka.infraestructure.bus.serialize.SuccessNotificationSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final EventListenerSubscriber eventListenerSubscriber;
    private final SocketController socketController;

    public RabbitMQConsumer(EventListenerSubscriber eventListenerSubscriber, SocketController socketController) {
        this.eventListenerSubscriber = eventListenerSubscriber;
        this.socketController = socketController;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "game.handler", durable = "true"),
            exchange = @Exchange(value = "hero.betting", type = "topic"),
            key = "game.#"
    ))
    public void recievedMessageGame(Message<String> message) {
        messageShared(message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "card.handler", durable = "true"),
            exchange = @Exchange(value = "hero.betting", type = "topic"),
            key = "card.#"
    ))
    public void recievedMessageCard(Message<String> message) {
        messageShared(message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "player.handler", durable = "true"),
            exchange = @Exchange(value = "hero.betting", type = "topic"),
            key = "player.#"
    ))
    public void recievedMessagePlayer(Message<String> message) {
        messageShared(message);
    }

    private void messageShared(Message<String> message) {
        var successNotification = SuccessNotificationSerializer.instance().deserialize(message.getPayload());
        var event = successNotification.deserializeEvent();
        log.info("Event arrived: {}", event.getAggregateName());
        try {
            this.eventListenerSubscriber.onNext(event);
            Optional.ofNullable(event.aggregateParentId()).ifPresentOrElse(id -> socketController.send(id, event),
                    () -> socketController.send(event.aggregateRootId(), event));
        } catch (Exception e) {
            this.eventListenerSubscriber.onError(e);
        }
    }
}
