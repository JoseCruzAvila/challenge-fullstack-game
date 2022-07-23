package co.com.sofka.event;

import org.springframework.context.ApplicationEvent;

public class AuditEvent<T> extends ApplicationEvent {
    private T entity;

    public AuditEvent(T entity) {
        super(entity);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
