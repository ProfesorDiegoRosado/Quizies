package ies.portadaalta.quizzengine.model.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import com.fasterxml.jackson.databind.ObjectMapper;
import ies.portadaalta.quizzengine.model.Question;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckJsonLoader {

    private final ObjectMapper mapper;

    public DeckJsonLoader() {
        this.mapper = new ObjectMapper();
    }

    public Deck loadFrom(String deckName, URL url) throws IOException {
        Deck deck = mapper.readValue(url, Deck.class);
        return deck;
    }

    public Deck loadFrom(String deckName, File file) throws IOException {
        Deck deck = mapper.readValue(file, Deck.class);
        return deck;
    }

    public Deck loadFromFile(String deckName, String filename) throws IOException {
        File file = new File(filename);
        return loadFrom(deckName, file);
    }

    public Deck loadFrom(String deckName, String jsonString) throws IOException {
        JsonNode jsonRootNode = mapper.readTree(jsonString);

        Map<Category, List<Question>> categoryQuestionsMap = new HashMap<>();
        for (int i = 0; i < jsonRootNode.size(); i++) {
            JsonNode jsonDeckCategoryNode = jsonRootNode.get(i);
            Category currentCategory = getCategoryFrom(jsonDeckCategoryNode, i);
            List<Question> questions = loadQuestionsFrom(currentCategory, jsonDeckCategoryNode);
            categoryQuestionsMap.put(currentCategory, questions);
        }

        Deck deck = new Deck(deckName, categoryQuestionsMap);
        return deck;
    }

    private Category getCategoryFrom(JsonNode jsonDeckCategoryNode, int index) {
        String categoryString = jsonDeckCategoryNode.get("category").asText();
        Category category = new Category(categoryString, categoryString, index);
        return category;
    }

    private List<Question> loadQuestionsFrom(Category category, JsonNode jsonDeckCategoryNode) {
        List<Question> questions = new ArrayList<>();
        JsonNode jsonQuestionsArrayNode = jsonDeckCategoryNode.get("questions");

        for (JsonNode jsonCurrentQuestionNode: jsonQuestionsArrayNode) {
            JsonNode jsonQuestionNode = jsonCurrentQuestionNode.get("question");
            String question = jsonQuestionNode.get("question").asText();
            int rightAnswer = jsonQuestionNode.get("rightAnswer").asInt();
            JsonNode jsonAnswersNode = jsonQuestionNode.get("answers");
            List<String> answers = loadAnswersFrom(jsonAnswersNode);
            Question currentQuestion = new Question(category, question, answers, rightAnswer);
            questions.add(currentQuestion);
        }
        return questions;
    }

    private List<String> loadAnswersFrom(JsonNode jsonAnswersNode) {
        List<String> answers = new ArrayList<>();
        for (JsonNode jsonAnswerNode: jsonAnswersNode) {
            String currentAnswer = jsonAnswerNode.asText();
            answers.add(currentAnswer);
        }
        return answers;
    }

}
