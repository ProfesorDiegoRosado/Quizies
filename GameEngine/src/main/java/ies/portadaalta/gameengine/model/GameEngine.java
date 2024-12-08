package ies.portadaalta.gameengine.model;

import ies.portadaalta.gameengine.model.observer.GameEnginePublisher;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.util.List;

public class GameEngine extends GameEnginePublisher {

    private String name;
    private Deck deck;
    private List<Player> players;

    // Constructor
    public GameEngine(String gameName, Deck deck, List<Player> players) {
        this.name = gameName;
        this.deck = deck;
        this.players = players;
    }

    // Methods
    public String getName() {
        return name;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void play() {
        notifyGameStarts();

        boolean end = false;
        int roundCount = 1;
        while (!end) {
            notifyRoundStarts(roundCount);
            playRound();
            notifyRoundEnds(roundCount);

            roundCount++;
            end = checkEnd();
        }

        notifyGameEnds(getWinners());
    }

    private void playRound() {
        for (Player player: players) {
            notifyTurnStarts(player);
            playTurn(player);
            notifyTurnEnds(player);
        }
    }

    private void playTurn(Player player) {
        Question question = deck.getNextRandomQuestion();
        notifyQuestion(question);

        int choice = player.getChoice();

        player.updateStats(question, choice);

        notifyPlayerChoice(player, question, choice);
    }

    private boolean checkEnd() {
        boolean end = false;
        List<Player> winners = getWinners();
        if (winners.size()>0) {
            end = true;
        }
        return end;
    }

    public List<Player> getWinners() {
        return players.stream().filter( Player::isWinner ).toList();
    }

    @Override
    public String toString() {
        return "GameEngine{" +
                "name='" + name + '\'' +
                ", Deck='" + deck + '\'' +
                ", players=" + players +
                '}';
    }
}
