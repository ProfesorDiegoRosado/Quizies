package ies.portadaalta.quizzengine.model.loaders;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckCsvLoader {

    public final static String CSV_HEADER = """
            category, question, rightAnswer, answers (variable column size)""";

    private final CsvMapper mapper;

    public DeckCsvLoader() {
        this.mapper = new CsvMapper();
    }

    public Deck loadFromFile(String deckName, File file) {
        return null;
    }

    public Deck loadFromFilename(String deckName, String filename) throws IOException {
        String fileContent = Files.readString(Paths.get(filename));
        return loadFromString(deckName, fileContent);
    }

    public Deck loadFromString(String deckName, String csvString) throws IOException {
        MappingIterator<List<String>> it = mapper
                .readerForListOf(String.class)
                .with(CsvParser.Feature.WRAP_AS_ARRAY) // !!! IMPORTANT
                .readValues(csvString);

        List<List<String>> csvContent = it.readAll();

        if (csvContent.size()==0) {
            throw new IOException("File contains no information");
        }

        List<String> headerLine = csvContent.get(0);
        assert headerLine.get(0).trim().toLowerCase().startsWith("category");
        csvContent.remove(0); // remove header line

        Deck deck = loadDeckFromCsvContent(csvContent);

        return deck;

    }

    private Deck loadDeckFromCsvContent(List<List<String>> csvContent) {

        Map<Category, List<Question>> categoryQuestionsMap = initCategoryQuestionsMap(csvContent);

        Deck deck = new Deck("Dummy Deck", categoryQuestionsMap);

        for (List<String> csvLine: csvContent) {
            Question question = createQuestionFrom(deck, csvLine);
            deck.addQuestion(question);
        }

        return deck;
    }

    private Map<Category, List<Question>> initCategoryQuestionsMap(List<List<String>> csvContent) {
        List<String> categoryNames = csvContent.stream().map(l -> l.get(0)).distinct().toList();

        Map<Category, List<Question>> categoryQuestionsMap = new HashMap<>();

        for (int i = 0; i < categoryNames.size(); i++) {
            String currentCategory = categoryNames.get(i);
            Category category = new Category(currentCategory, currentCategory, i);
            categoryQuestionsMap.put(category, new ArrayList<>());
        }

        return categoryQuestionsMap;
    }

    private Question createQuestionFrom(Deck deck, List<String> csvLine) {
        // extract data from csvLine
        String categoryName = csvLine.get(0);
        String questionString = csvLine.get(1);
        int rightAnswer = Integer.parseInt(csvLine.get(2));
        List<String> answers = createAnswersFrom(csvLine);
        // Create question
        Category category = deck.getCategoryWithName(categoryName);
        Question question = new Question(category, questionString, answers, rightAnswer);
        return question;
    }

    private List<String> createAnswersFrom(List<String> csvLine) {
        List<String> answers = new ArrayList<>();
        for (int i = 3; i < csvLine.size(); i++) {
            answers.add(csvLine.get(i));
        }
        return answers;
    }

}
