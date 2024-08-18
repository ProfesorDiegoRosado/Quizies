package ies.portadaalta.quizzengine.model.loaders;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckXmlLoader {

    public final static String ROOT_ELEMENT_TAG = "quizies";
    public final static String CATEGORY_ELEMENT_TAG = "category";
    public final static String CATEGORY_ELEMENT_NAME_ATTR = "name";
    public final static String QUESTIONS_ELEMENT_TAG = "questions";
    public final static String QUESTION_ELEMENT_TAG = "question";
    public final static String QUESTION_ELEMENT_QUESTION_ATTR = "question";
    public final static String ANSWERS_ELEMENT_TAG = "answers";
    public final static String ANSWER_ELEMENT_TAG = "answer";
    public final static String ANSWER_ELEMENT_RIGHTANSWER_ATTR = "rightAnswer";


    public DeckXmlLoader() {}


    public Deck loadFromUrl(String deckName, URL url) throws IOException {
        // TODO: to be implemented
        return null;
    }

    public Deck loadFromFile(String deckName, File file) throws IOException, JDOMException {
        return loadFromFilename(deckName, file.getAbsolutePath().toString());
    }

    public Deck loadFromFilename(String deckName, String filename) throws IOException, JDOMException {
        String fileContent = Files.readString(Paths.get(filename));
        return loadFromString(deckName, fileContent);
    }

    public Deck loadFromString(String deckName, String xmlString) throws IOException, JDOMException {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(new StringReader(xmlString));

        Element rootNode = doc.getRootElement();
        List<Element> categoriesElement = rootNode.getChildren();

        Map<Category, List<Question>> categoryQuestionsMap = getCategoryQuestionsMap(categoriesElement);

        Deck deck = new Deck(deckName, categoryQuestionsMap);
        return deck;
    }

    private Map<Category, List<Question>> getCategoryQuestionsMap(List<Element> categoriesElement) throws JDOMException {
        Map<Category, List<Question>> categoryQuestionsMap = new HashMap<>();

        for (int i = 0; i < categoriesElement.size(); i++) {
            Element currentCategoryElement = categoriesElement.get(i);
            String categoryName = currentCategoryElement.getAttributeValue(CATEGORY_ELEMENT_NAME_ATTR);

            Category currentCategory = new Category(categoryName, categoryName, i);
            Element questionsElement = currentCategoryElement.getChild(QUESTIONS_ELEMENT_TAG);
            List<Element> questionsListElement = questionsElement.getChildren();
            List<Question> questions = getQuestions(questionsListElement, currentCategory);

            categoryQuestionsMap.put(currentCategory, questions);
        }
        return categoryQuestionsMap;
    }

    private List<Question> getQuestions(List<Element> questionsListElement, Category currentCategory) throws JDOMException {
        List<Question> questions = new ArrayList<>();
        for (Element questionElement: questionsListElement) {
            String questionString = questionElement.getAttributeValue(QUESTION_ELEMENT_QUESTION_ATTR);

            Element answersElement = questionElement.getChild(ANSWERS_ELEMENT_TAG);

            List<Element> answersListElement = answersElement.getChildren();
            Tuple2<List<String>, Integer> answersTuple = getAnswers(answersListElement);
            Question currentQuestion = new Question(currentCategory, questionString, answersTuple._1(), answersTuple._2());

            questions.add(currentQuestion);
        }
        return questions;
    }

    private Tuple2<List<String>, Integer> getAnswers(List<Element> answersListElement) throws JDOMException {
        List<String> answers = new ArrayList<>();
        int rightAnswer = 0;

        for (int j = 0; j < answersListElement.size(); j++) {
            Element currentAnswerElement = answersListElement.get(j);
            Attribute rightAnswerAttribute = currentAnswerElement.getAttribute(ANSWER_ELEMENT_RIGHTANSWER_ATTR);
            if (rightAnswerAttribute!=null && rightAnswerAttribute.getBooleanValue()) {
                rightAnswer = j;
            }
            String answerString = currentAnswerElement.getText();

            answers.add(answerString);
        }
        Tuple2<List<String>, Integer> answersTuple = Tuple.of(answers, rightAnswer);
        return answersTuple;
    }


}
