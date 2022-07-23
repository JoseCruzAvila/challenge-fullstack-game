package co.com.sofka.model.events;

public class DomainEvent {
    private final String type;

    public DomainEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
