package co.com.sofka.bus;

import co.com.sofka.generic.events.DomainEvent;
import com.google.gson.Gson;
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

    public void publish(DomainEvent event) {
        log.info("Event received: {}", event.getType());
        rabbitTemplate.convertAndSend(exchange.getName(), event.getType(), new Gson().toJson(event));
    }
}
