package ies.portadaalta.gameengine.model;

import ies.portadaalta.gameengine.model.stats.CategoryStats;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.util.List;
import java.util.Map;
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

        System.out.println("\n\n-----------------------------------------------------------------------");

        System.out.println( String.format("Turno de Jugador %s", player.getName()) );

        Question question = deck.getNextRandomQuestion();

        System.out.println( String.format("Pregunta de la categoria %s, con color %s",
                question.getCategory().getName(),
                question.getCategory().getColor().getHexString())
        );

        printQuestion(question);
        int choice = getChoice();

        if (question.isValidAnswer(choice-1)) {
            System.out.println("    >>> Respuesta correcta");
        } else {
            System.out.println("    >>> Respuesta INcorrecta");
        }

        player.updateStats(question, choice);

        showStats(player);

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
            System.out.println( String.format("\n   ---> Los ganadores son %s", winnersString) );
        }
        return end;
    }

    public List<Player> getWinners() {
        return players.stream().filter( Player::isWinner ).toList();
    }

    private void showStats(Player player) {
        Map<Category, CategoryStats> statsMap = player.getCategoryStats();
        System.out.println(" -- Player '" + player.getName() + "' Stats --");
        statsMap.entrySet().stream().forEach( entry -> {
            Category category = entry.getKey();
            CategoryStats categoryStats = entry.getValue();

            int numberOfQuestions = categoryStats.getNumberOfQuestions();
            int rightAnswered = categoryStats.getRightAnswered();
            double successAnswerRate = categoryStats.getSuccessAnswerRate();

            System.out.println("   -> Category " + category.getName());
            System.out.println("      - Number of asked questions: " + numberOfQuestions);
            System.out.println("      - Number of rightAnswered questions: " + rightAnswered);
            System.out.println("      - Succesfully answered questions rate: " + (successAnswerRate*100) + "%");
        });
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
