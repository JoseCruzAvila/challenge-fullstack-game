package co.com.sofka.mongo.player;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
public class PlayerDocument {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private Double points;
    private Set<String> cards;
}
