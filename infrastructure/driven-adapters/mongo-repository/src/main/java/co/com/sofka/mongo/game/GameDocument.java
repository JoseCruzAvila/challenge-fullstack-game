package co.com.sofka.mongo.game;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
public class GameDocument {
    @Id
    private String id;
    @Indexed(unique = true)
    private String gameId;
    private Boolean playing;
    private ObjectId winnerId;
    private Set<ObjectId> playersList;
}
