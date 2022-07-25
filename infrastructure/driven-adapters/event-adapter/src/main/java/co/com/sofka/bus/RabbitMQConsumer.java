package co.com.sofka.bus;

import co.com.sofka.websocket.SocketHandler;
import co.com.sofka.websocket.SocketSender;
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
    //private SocketSender socketSender;
    private SocketHandler socketHandler;

    public RabbitMQConsumer(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "game.handler", durable = "true"),
            exchange = @Exchange(value = "fullstack.game", type = "topic"),
            key = "game.#"
    ))
    public void gameMessageReceived(Message<String> message) {
        messageShared(message);
    }

    private void messageShared(Message<String> message) {
        JSONObject event = new JSONObject(message.getPayload());
        log.info("Message received: {}", message.getPayload());
        socketHandler.sendMessage(event.getJSONObject("source"));
        /*socketSender.sendMessage("localhost:8080/retrieve/".concat(event.getJSONObject("source")
                .getString("gameId")), message.getPayload());*/
    }
}
