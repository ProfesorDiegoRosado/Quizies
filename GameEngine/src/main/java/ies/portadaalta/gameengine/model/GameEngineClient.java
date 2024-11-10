package ies.portadaalta.gameengine.model;

import ies.portadaalta.gameengine.model.observer.GameEngineObserver;
import ies.portadaalta.gameengine.model.stats.CategoryStats;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Question;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class GameEngineClient implements GameEngineObserver {

    private final static DecimalFormat RATE_DECIMAL_FORMAT = new DecimalFormat("#.00");

    public GameEngineClient() { }

    @Override
    public void notifyGameStarts() {
        System.out.println("\n -- Empieza el juego -- \n");
    }

    @Override
    public void notifyGameEnds(List<Player> winnerPlayers) {
        System.out.println("\n  ---> El juego ha terminado.");
        String winnersString = String.join(",", winnerPlayers.stream().map(Player::getName).toList());
        System.out.println( String.format("\n   ---> Los ganadores son %s", winnersString) );
    }

    @Override
    public void notifyRoundStarts(int roundCount) {
        System.out.println("Empieza la ronda " + roundCount);
    }

    @Override
    public void notifyRoundEnds(int roundCount) {
        System.out.println("La ronda " + roundCount + " ha terminado");
    }

    @Override
    public void notifyTurnStarts(Player player) {
        System.out.println("\n\n------------------------------------------------");
        System.out.println("Turno del judador " + player.getName());
    }

    @Override
    public void notifyPlayerChoice(Player player, Question question, int choice) {
        printChoiceOutcome(question, choice);
        printStats(player);
    }

    private void printChoiceOutcome(Question question, int choice) {
        if (question.isValidAnswer(choice)) {
            System.out.println("    >>> Respuesta correcta");
        } else {
            System.out.println("    >>> Respuesta IN-correcta");
        }
        System.out.println();
    }

    private void printStats(Player player) {
        Map<Category, CategoryStats> statsMap = player.getCategoryStats();
        System.out.println(" -- Player '" + player.getName() + "' Stats --");
        statsMap.entrySet().stream().forEach( entry -> {
            Category category = entry.getKey();
            CategoryStats categoryStats = entry.getValue();

            int numberOfQuestions = categoryStats.getNumberOfQuestions();
            int rightAnswered = categoryStats.getRightAnswered();
            double successAnswerRate = categoryStats.getSuccessAnswerRate();

            System.out.println("   -> Categoria " + category.getName());
            System.out.println("      - Número de preguntas realizadas: " + numberOfQuestions);
            System.out.println("      - Número de preguntas contestadas correctamente: " + rightAnswered);
            System.out.println("      - Porcentaje de preguntas contestadas correctamente: " +
                    RATE_DECIMAL_FORMAT.format(successAnswerRate*100) + "%");
        });
    }

    @Override
    public void notifyQuestion(Question question) {
        printQuestionTitle(question);
        printQuestion(question);
    }

    private void printQuestionTitle(Question question) {
        System.out.println( String.format("Pregunta de la categoria %s, con color %s",
                question.getCategory().getName(),
                question.getCategory().getColor().getHexString())
        );
    }

    private void printQuestion(Question question) {
        System.out.println( String.format(" * %s", question.getQuestion()) );
        List<String> answers = question.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            System.out.println( String.format("   %s) %s", i+1, answers.get(i)) );
        }
        System.out.println("Elija opción: ");
    }

    @Override
    public void notifyTurnEnds(Player player) {
        System.out.println(" -- Fin turno del jugador " + player.getName());
        System.out.println("\n");
    }
}
