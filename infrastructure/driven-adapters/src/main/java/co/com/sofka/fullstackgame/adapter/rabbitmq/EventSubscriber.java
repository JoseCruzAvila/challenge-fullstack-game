package co.com.sofka.fullstackgame.adapter.rabbitmq;

public interface EventSubscriber {
    void subscribe(String eventType, String exchange);
}