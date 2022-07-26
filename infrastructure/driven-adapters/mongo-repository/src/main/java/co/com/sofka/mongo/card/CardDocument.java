package co.com.sofka.mongo.card;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CardDocument {
    @Id
    private String id;
    private String image;
    private Integer power;
    private String description;
}
