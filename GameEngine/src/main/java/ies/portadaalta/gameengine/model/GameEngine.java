package ies.portadaalta.gameengine.model;

import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.util.List;
import java.util.Scanner;

public class GameEngine {

    private String name;
    private Deck deck;
    private List<Player> players;

    public GameEngine(String name, Deck deck, List<Player> players) {
        this.name = name;
        this.deck = deck;
        this.players = players;
    }

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

        boolean end = false;
        while (!end) {
            playRound();
            end = checkEnd();
        }
    }

    private void playRound() {
        for (Player player: players) {
            playTurn(player);
        }
    }

    private void playTurn(Player player) {

        System.out.println( String.format("Turno de Jugador %s", player.getName()) );

        Question question = deck.getNextRandomQuestion();

        System.out.println( String.format("Pregunta de la categoria %s, con color %s",
                question.getCategory().getName(),
                question.getCategory().getColor().getHexString())
        );

        printQuestion(question);
        int choice = getChoice();

        player.updateStats(question, choice);

    }

    private void printQuestion(Question question) {
        System.out.println( String.format(" * %s", question.getQuestion()) );
        List<String> answers = question.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            System.out.println( String.format("   %s) %s", i+1, answers.get(i)) );
        }
        System.out.println("Choose option: ");
    }

    private int getChoice() {
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        return i;
    }

    private boolean checkEnd() {
        boolean end = false;
        List<Player> winners = getWinners();
        if (winners.size()>0) {
            end = true;
            String winnersString = String.join(",", winners.stream().map(Player::getName).toList());
            System.out.println( String.format("Los ganadores son %s", winnersString) );
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
