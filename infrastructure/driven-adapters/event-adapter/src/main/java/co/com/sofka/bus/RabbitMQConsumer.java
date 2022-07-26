package co.com.sofka.bus;

import co.com.sofka.websocket.SocketController;
import org.json.JSONObject;
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
    private final SocketController socketController;

    public RabbitMQConsumer(SocketController socketController) {
        this.socketController = socketController;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "game.socket.handler", durable = "true"),
            exchange = @Exchange(value = "fullstack.game", type = "fanout"),
            key = "game.#"
    ))
    public void gameMessageReceived(Message<String> message) {
        messageShared(message);
    }

    private void messageShared(Message<String> message) {
        JSONObject currentEvent = new JSONObject(message.getPayload());
        log.info("Message received: {}", message.getPayload());
        socketController.send(currentEvent.getString("aggregateId"), message.getPayload());
    }
}
