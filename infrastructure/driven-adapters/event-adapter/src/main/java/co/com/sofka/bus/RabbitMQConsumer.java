package co.com.sofka.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "game.handler", durable = "true"),
            exchange = @Exchange(value = "fullstack.game", type = "topic"),
            key = "game.#"
    ))
    public void gameMessageReceived(Message<String> message) {
        log.info("Message received: {}", message.getPayload());
    }
}
