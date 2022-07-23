package co.com.sofka.fullstackgame.adapter.rabbitmq;

import co.com.sofka.business.generic.BusinessException;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.fullstackgame.config.ApplicationConfig;
import co.com.sofka.infraestructure.bus.EventBus;
import co.com.sofka.infraestructure.bus.notification.ErrorNotification;
import co.com.sofka.infraestructure.bus.notification.SuccessNotification;
import co.com.sofka.infraestructure.bus.serialize.ErrorNotificationSerializer;
import co.com.sofka.infraestructure.bus.serialize.SuccessNotificationSerializer;
import co.com.sofka.infraestructure.event.ErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventBus implements EventBus{

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventBus.class);
    private static final String TOPIC_ERROR = "hero.betting.error";
    private static final String TOPIC_BUSINESS_ERROR = "hero.betting.business.error";
    private final RabbitTemplate rabbitTemplate;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public RabbitMQEventBus(RabbitTemplate rabbitTemplate, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        var notification = SuccessNotification.wrapEvent(ApplicationConfig.ORIGIN, event);
        var notificationSerialization = SuccessNotificationSerializer.instance().serialize(notification);
        log.info("Publish: {}", notificationSerialization);
        rabbitTemplate.convertAndSend(ApplicationConfig.ORIGIN, event.type, notificationSerialization.getBytes());
        reactiveMongoTemplate.save(event, event.type);
    }

    @Override
    public void publishError(ErrorEvent errorEvent) {

        if (errorEvent.error instanceof BusinessException) {
            publishToTopic(TOPIC_BUSINESS_ERROR, errorEvent);
        } else {
            publishToTopic(TOPIC_ERROR, errorEvent);
        }
        log.info(errorEvent.error.getMessage());
    }

    public void publishToTopic(String topic, ErrorEvent errorEvent) {
        var notification = ErrorNotification.wrapEvent(ApplicationConfig.ORIGIN, errorEvent);
        var notificationSerialization = ErrorNotificationSerializer.instance().serialize(notification);
        rabbitTemplate.convertAndSend(topic + "." + errorEvent.identify, notificationSerialization.getBytes());
        log.info("###### Error Event published to {}", topic);
    }
}
