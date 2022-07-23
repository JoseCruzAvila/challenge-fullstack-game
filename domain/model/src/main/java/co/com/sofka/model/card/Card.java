package co.com.sofka.model.card;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {
    private String id;
    private String image;
    private String description;
}
