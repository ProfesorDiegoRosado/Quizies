package ies.portadaalta.quizzengine.model.converters;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeckJsonConverter {

    private final static String CSV_HEADER = """
            category, question, rightAnswer, answers (variable column size)""";

    public DeckJsonConverter() { }

    public void convertoJson2Csv(String jsonFilename, String csvFilename) throws IOException {
        DeckJsonLoader jsonLoader = new DeckJsonLoader();
        Deck deck = jsonLoader.loadFromFilename("Dummy Deck", jsonFilename);
        writeDeck2Csv(deck, csvFilename);
    }

    private void writeDeck2Csv(Deck deck, String csvFilename) throws IOException {
        List<String> csvLines = conver2Csv(deck);
        write2CsvFilename(csvLines, csvFilename);
    }

    // Each String of the list is a line
    private List<String> conver2Csv(Deck deck) {
        List<String> csvLines = new ArrayList<>();
        csvLines.add(CSV_HEADER);

        List<String> deckCsvLines = getCsvLinesFromDeck(deck);
        csvLines.addAll(deckCsvLines);

        return csvLines;
    }

    private void write2CsvFilename(List<String> csvLines, String csvFilename) throws IOException {
        Files.write(Paths.get(csvFilename), csvLines, StandardCharsets.UTF_8);
    }

    private List<String> getCsvLinesFromDeck(Deck deck) {
        List<String> csvLines = new ArrayList<>();

        Set<Category> categories = deck.getCategories();
        for (Category category: categories) {
            List<Question> questionsForCategory = deck.getQuestionsForCategory(category);

            List<String> csvLinesForCategory = makeCsvLinesFrom(category, questionsForCategory);
            csvLines.addAll(csvLinesForCategory);
        }

        return csvLines;
    }

    private List<String> makeCsvLinesFrom(Category category, List<Question> questions) {
        List<String> csvLinesForCategory = new ArrayList<>();
        for (Question question: questions) {
            String csvLine = makeCsvLineFromQuestion(category, question);
            csvLinesForCategory.add(csvLine);
        }
        return csvLinesForCategory;
    }

    private String makeCsvLineFromQuestion(Category category, Question question) {
        StringBuilder stringBuilder = new StringBuilder();
        appendBetweenQuotes(stringBuilder, category.getName());
        appendBetweenQuotes(stringBuilder, question.getQuestion());
        appendBetweenQuotes(stringBuilder, Integer.toString(question.getRightAnswer()));
        for (int i = 0; i < question.getAnswers().size(); i++) {
            if (!isLastAnswer(question, i)) {
                appendBetweenQuotes(stringBuilder, question.getAnswers().get(i)); // append comma
            } else { // it is last answer
                appendBetweenQuotes(stringBuilder, question.getAnswers().get(i), false); // do not append trailing comma
            }
        }
        return stringBuilder.toString();
    }

    private boolean isLastAnswer(Question question, int i) {
        return question.getAnswers().size()==i+1;
    }

    private void appendBetweenQuotes(StringBuilder stringBuilder, String s) {
        appendBetweenQuotes(stringBuilder, s, true);
    }

    private void appendBetweenQuotes(StringBuilder stringBuilder, String s, boolean appendComma) {
        stringBuilder.append("\"").append(s).append("\"");
        if (appendComma) {
            stringBuilder.append(",");
        }
    }

}
