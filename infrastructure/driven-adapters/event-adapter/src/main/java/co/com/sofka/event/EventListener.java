package co.com.sofka.event;

import co.com.sofka.bus.RabbitMQEventBus;
import co.com.sofka.generic.events.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener implements ApplicationListener<AuditEvent> {
    @Autowired
    private RabbitMQEventBus bus;

    @Override
    public void onApplicationEvent(AuditEvent event) {
        DomainEvent entity = (DomainEvent) event.getEntity();
        bus.publish(entity);
    }
}
