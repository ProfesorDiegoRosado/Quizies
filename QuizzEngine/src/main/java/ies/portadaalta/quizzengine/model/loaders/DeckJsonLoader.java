package ies.portadaalta.quizzengine.model.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Color;
import ies.portadaalta.quizzengine.model.Deck;
import com.fasterxml.jackson.databind.ObjectMapper;
import ies.portadaalta.quizzengine.model.Question;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckJsonLoader {

    private final ObjectMapper mapper;

    public DeckJsonLoader() {
        this.mapper = new ObjectMapper();
    }

    public Deck loadFromUrl(String deckName, URL url) throws IOException {
        // TODO: to be implemented
        return null;
    }

    public Deck loadFromFile(String deckName, File file) throws IOException {
        return loadFromFilename(deckName, file.getAbsolutePath().toString());
    }

    public Deck loadFromFilename(String deckName, String filename) throws IOException {
        String fileContent = Files.readString(Paths.get(filename));
        return loadFromString(deckName, fileContent);
    }

    public Deck loadFromString(String deckName, String jsonString) throws IOException {
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
        JsonNode colorNode = jsonDeckCategoryNode.get("color");
        Category category;
        if (colorNode!=null) {
            String hexColorString = colorNode.asText();
            category = new Category(categoryString, categoryString, hexColorString);
        } else {
            category = new Category(categoryString, categoryString, index);
        }
        return category;
    }

    private List<Question> loadQuestionsFrom(Category category, JsonNode jsonDeckCategoryNode) {
        List<Question> questions = new ArrayList<>();
        JsonNode jsonQuestionsArrayNode = jsonDeckCategoryNode.get("questions");

        for (JsonNode jsonCurrentQuestionNode: jsonQuestionsArrayNode) {
            JsonNode jsonQuestionNode = jsonCurrentQuestionNode.get("question");
            String question = jsonQuestionNode.get("question").asText();
            //System.out.println("Question: " + question);
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
