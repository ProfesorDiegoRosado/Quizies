package ies.portadaalta.quizzengine.model.converters;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import static ies.portadaalta.quizzengine.model.loaders.DeckCsvLoader.CSV_HEADER;
import static ies.portadaalta.quizzengine.model.loaders.DeckXmlLoader.*;


public class DeckJsonConverter {


    public DeckJsonConverter() { }


    // CSV converter
    public void writeDeck2Csv(Deck deck, String csvFilename) throws IOException {
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

    // Xml converter

    public void write2Xml(Deck deck, String xmlFilename) throws IOException {
        Document doc = getDomDocument(deck);

        File file = new File(xmlFilename);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        writeDoc2OutputStream(doc, fileOutputStream);
    }

    private void writeDoc2OutputStream(Document doc, OutputStream outputStream) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        xmlOutputter.output(doc, outputStream);
    }

    private Document getDomDocument(Deck deck) {
        Document doc = new Document();

        Element quiziesElement = new Element(ROOT_ELEMENT_TAG);
        doc.setRootElement(quiziesElement);

        Set<Category> categories = deck.getCategories();

        for (Category category : categories) {

            Element categoryElement = new Element(CATEGORY_ELEMENT_TAG);
            categoryElement.setAttribute(CATEGORY_ELEMENT_NAME_ATTR, category.getName());

            Element questionsElement = createQuestionsElement(deck, category);

            categoryElement.addContent(questionsElement);

            quiziesElement.addContent(categoryElement);
        }
        return doc;
    }

    private Element createQuestionsElement(Deck deck, Category category) {
        Element questionsElement = new Element(QUESTIONS_ELEMENT_TAG);

        List<Question> questionsForCategory = deck.getQuestionsForCategory(category);

        for (Question question : questionsForCategory) {
            String questionString = question.getQuestion();
            int rightAnswer = question.getRightAnswer();
            List<String> answers = question.getAnswers();

            Element questionElement = createQuestionElement(questionString, answers, rightAnswer);

            questionsElement.addContent(questionElement);
        }
        return questionsElement;
    }

    private Element createQuestionElement(String questionString, List<String> answers, int rightAnswer) {
        Element questionElement = new Element(QUESTION_ELEMENT_TAG);
        questionElement.setAttribute(QUESTION_ELEMENT_QUESTION_ATTR, questionString);

        Element answersElement = createAnswersElement(answers, rightAnswer);
        questionElement.addContent(answersElement);

        return questionElement;
    }

    private Element createAnswersElement(List<String> answers, int rightAnswer) {
        Element answersElement = new Element(ANSWERS_ELEMENT_TAG);

        for (int i = 0; i < answers.size(); i++) {
            Element answerElement = createAnswerElement(answers, rightAnswer, i);
            answersElement.addContent(answerElement);
        }
        return answersElement;
    }

    private Element createAnswerElement(List<String> answers, int rightAnswer, int index) {
        Element answerElement = new Element(ANSWER_ELEMENT_TAG);
        answerElement.setText(answers.get(index));
        if (index == rightAnswer) {
            answerElement.setAttribute(ANSWER_ELEMENT_RIGHTANSWER_ATTR, "true");
        }
        return answerElement;
    }
    
}
