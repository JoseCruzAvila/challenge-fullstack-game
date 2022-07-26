package co.com.sofka.model.game;

import co.com.sofka.model.card.Card;
import co.com.sofka.model.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
public class Game {
    private String id;
    private String gameId;
    private Boolean playing;
    private Player winner;
    private Set<Player> players;

    public Game() {
        this.playing = false;
        this.winner = null;
        this.players = Set.of();
    }

    public Game(String id, Player player) {
        this.gameId = id;
        this.playing = false;
        this.winner = null;
        this.players = Set.of(player);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void splitCards(List<Card> cards) {
        this.players.forEach(player -> player.addCardToDeck(randomCard(cards)));
    }

    private Card randomCard(List<Card> cards) {
        Random random = new Random();
        Card cardSelected = cards.get(random.nextInt(0, cards.size()));
        cards.remove(cardSelected);

        return cardSelected;
    }
}
