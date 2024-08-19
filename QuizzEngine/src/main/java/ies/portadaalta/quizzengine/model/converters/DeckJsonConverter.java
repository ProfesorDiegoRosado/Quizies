package ies.portadaalta.quizzengine.model.converters;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.sqlite.SQLiteException;

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

    // SQLite converter
    public void writeDeck2Sqlite(Deck deck, String dbName) throws SQLException {

        String url = "jdbc:sqlite:" + dbName;

        try (Connection conn = DriverManager.getConnection(url)) {
            createSchema(conn);
        }

        try (Connection conn = DriverManager.getConnection(url)) {
            saveData(conn, deck);
        }
    }

    private void createSchema(Connection conn) throws SQLException {

        String createTableCategory = """
                CREATE TABLE IF NOT EXISTS Category(
                    name text,
                    description text,
                    CONSTRAINT PK_Category PRIMARY KEY(name)
                )
                """;

        String createTableQuestion = """
                CREATE TABLE IF NOT EXISTS Question(
                    category text,
                    question text,
                    CONSTRAINT PK_Question PRIMARY KEY(question),
                    CONSTRAINT FK_Question_Category FOREIGN KEY(category) REFERENCES Category(name)
                ) 
                """;

        String createTableAnswers = """
                CREATE TABLE IF NOT EXISTS Answer(
                    answer text,
                    question text,
                    rightAnswer integer,
                    CONSTRAINT PK_Answer PRIMARY KEY(question, answer),
                    CONSTRAINT FK_Answer_Question FOREIGN KEY(question) REFERENCES Question(question)
                )
                """;


        Statement statement = conn.createStatement();
        statement.executeUpdate(createTableCategory);
        statement.executeUpdate(createTableQuestion);
        statement.executeUpdate(createTableAnswers);
    }

    private void saveData(Connection conn, Deck deck) throws SQLException {

        String name = deck.getName();

        Set<Category> categories = deck.getCategories();
        saveCategories(conn, categories);
        saveQuestions(conn, deck);

    }

    private void saveQuestions(Connection conn, Deck deck) throws SQLException {

        for (Category category: deck.getCategories()) {

            List<Question> questions = deck.getQuestionsForCategory(category);

            saveQuestionsForCategory(conn, questions, category);
        }

    }

    private void saveQuestionsForCategory(Connection conn, List<Question> questions, Category category) throws SQLException {
        String sql = "INSERT INTO Question(category, question) VALUES(?,?)";
        PreparedStatement preStatement = conn.prepareStatement(sql);


        questions.stream().forEach( question -> {
            try {
                preStatement.setString(1, question.getCategory().getName());
                preStatement.setString(1, question.getQuestion());

                preStatement.executeUpdate();

                saveAnswers(conn, question);

            } catch (SQLiteException sqliteException) {
                if (sqliteException.getResultCode().message.contains("A PRIMARY KEY constraint failed")) { // Primary key already in use
                    System.out.println("Primary key already in use for (" + question.getCategory().getName() + ", " + question.getQuestion() + ")");
                } else {
                    throw new RuntimeException(sqliteException);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveAnswers(Connection conn, Question question) throws SQLException {
        String questionString = question.getQuestion();
        List<String> answers = question.getAnswers();
        int rightAnswer = question.getRightAnswer();

        String sql = "INSERT INTO Answer(answer, question, rightAnswer) VALUES(?,?,?)";
        PreparedStatement preStatement = conn.prepareStatement(sql);

        IntStream.range(0, answers.size()).forEach( i -> {
            try {
                String answer = answers.get(i);
                preStatement.setString(1, answer);
                preStatement.setString(2, questionString);
                preStatement.setInt(3, (i==rightAnswer) ? 1 : 0);

                preStatement.executeUpdate();

            } catch (SQLiteException sqliteException) {
                if (sqliteException.getResultCode().message.contains("A PRIMARY KEY constraint failed")) { // Primary key already in use
                    System.out.println("Primary key already in use for (" + question.getCategory().getName() + ", " + question.getQuestion() + ")");
                } else {
                    throw new RuntimeException(sqliteException);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void saveCategories(Connection conn, Collection<Category> categories) throws SQLException {
        for (Category category: categories) {

            String sql = "INSERT INTO Category(name, description) VALUES(?,?)";
            PreparedStatement preStatement = conn.prepareStatement(sql);

            categories.stream().forEach( currentCategory -> {

                try {
                    preStatement.setString(1, currentCategory.getName());
                    preStatement.setString(2, currentCategory.getName());

                    preStatement.executeUpdate();

                } catch (SQLiteException sqliteException) {
                    if (sqliteException.getResultCode().message.contains("A PRIMARY KEY constraint failed")) { // Primary key already in use
                        System.out.println("Primary key already in use for " + currentCategory.getName());
                    } else {
                        throw new RuntimeException(sqliteException);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }

}
