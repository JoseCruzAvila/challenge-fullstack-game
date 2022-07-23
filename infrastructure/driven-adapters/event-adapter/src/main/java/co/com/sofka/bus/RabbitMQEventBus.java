package co.com.sofka.bus;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventBus {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventBus.class);
    private final RabbitTemplate rabbitTemplate;
    private final Exchange exchange;

    public RabbitMQEventBus(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void publish(String event) {
        String eventType =  new JSONObject(event).getString("type");
        log.info("Event received: {}", eventType);
        rabbitTemplate.convertAndSend(exchange.getName(), eventType, event);
    }
}
