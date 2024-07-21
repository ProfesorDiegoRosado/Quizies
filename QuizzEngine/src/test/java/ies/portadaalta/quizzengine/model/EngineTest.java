package ies.portadaalta.quizzengine.model;

import ies.portadaalta.quizzengine.exception.NoCategoryFoundException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static ies.portadaalta.quizzengine.model.Color.*;
import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private static final int RANDOM_SEED = 0;

    private static final Category geographyCategory = new Category("Geography", "Questions about Geography", BLUE);
    private static final Category artCategory = new Category("Arts and Literature", "Questions about Arts and Literature", PURPLE);
    private static final Category historyCategory = new Category("History", "Questions about History", YELLOW);
    private static final Category entertainmentCategory = new Category("Entertainment", "Questions about Entertainment", PINK);
    private static final Category scienceCategory = new Category("Science", "Questions about Science", GREEN);
    private static final Category sportsCategory = new Category("Sports", "Questions about Sports", ORANGE);



    @Test
    void catetoriesTest() {

        Engine engine = getEngineForScenario1();

        Set<Category> categories = engine.getCategories();
        Set<String> categoriesNames = categories.stream().map(Objects::toString).collect(Collectors.toSet());

        // Testing categories
        assertTrue(categoriesNames.add(geographyCategory.getName()));
        assertTrue(categoriesNames.add(artCategory.getName()));
        assertTrue(categoriesNames.add(historyCategory.getName()));
        assertTrue(categoriesNames.add(entertainmentCategory.getName()));
        assertTrue(categoriesNames.add(scienceCategory.getName()));
        assertTrue(categoriesNames.add(sportsCategory.getName()));

    }

    @Test
    void nextQuestionTest() {

        Engine engine = getEngineForScenario1();

        // Testing nextQuestion()
        Question geographyNextQuestion1 = engine.getNextQuestion(geographyCategory);
        assertTrue(geographyNextQuestion1.getQuestion().contains("Which country has the largest population in the world?"));

        // Same question as before (due to random)
        Question geographyNextQuestion2 = engine.getNextQuestion(geographyCategory);
        assertTrue(geographyNextQuestion2.getQuestion().contains("Which country has the largest population in the world?"));

        Question geographyNextQuestion3 = engine.getNextQuestion(geographyCategory);
        assertTrue(geographyNextQuestion3.getQuestion().contains("What is the name of the tallest mountain in the world?"));

    }


    @Test
    void nextQuestionNonExistingCategoryTest() {
        Engine engine = getEngineForScenario1();
        Category dummyCategory = new Category("Dummy", "Dummy category for testing", BLUE);

        //
        Exception exception = assertThrows(
                NoCategoryFoundException.class,
                () -> {
                    engine.getNextQuestion(dummyCategory);
                }
        );

        String expectedMessage = "The category Category{name='Dummy'";

        assertTrue(exception.getMessage().startsWith(expectedMessage));

    }

    @Test
    void isValidAnswerRightTest() {
        Engine engine = getEngineForScenario1();

        Question nextQuestion = engine.getNextQuestion(scienceCategory);
        assertTrue(nextQuestion.getQuestion().contains("Which is the most abundant element in the universe?"));
        assertTrue(nextQuestion.isValidAnswer(2));
    }

    @Test
    void isValidAnswerWrongTest() {
        Engine engine = getEngineForScenario1();

        Question nextQuestion = engine.getNextQuestion(scienceCategory);
        assertTrue(nextQuestion.getQuestion().contains("Which is the most abundant element in the universe?"));
        assertFalse(nextQuestion.isValidAnswer(0));
    }

    private Engine getEngineForScenario1() {

        Deck deck = new Deck("Scenario1 Deck", getCategoryListQuestionsMap(), new Random(RANDOM_SEED));

        Engine engine = new Engine(deck);

        return engine;
    }

    private Map<Category, List<Question>> getCategoryListQuestionsMap() {
        /*
            Geografía (Azul)
            Arte y Literatura (Morado)
            Historia (Amarillo)
            Entretenimiento (Rosa)
            Ciencias y Naturaleza (Verde)
            Deportes y Pasatiempos (Naranja)
         */
        //Category geographyCategory = new Category("Geography", "Questions about Geography", BLUE);
        //Category artCategory = new Category("Arts and Literature", "Questions about Arts and Literature", PURPLE);
        //Category historyCategory = new Category("History", "Questions about History", YELLOW);
        //Category entertainmentCategory = new Category("Entertainment", "Questions about Entertainment", PINK);
        //Category scienceCategory = new Category("Science", "Questions about Science", GREEN);
        //Category sportsCategory = new Category("Sports", "Questions about Sports", ORANGE);

        // Geography
        Question questionGeography1 = new Question(
                geographyCategory,
                "What is the name of the tallest mountain in the world?",
                List.of(
                        "Mount Everest",
                        "Teide",
                        "Annapurna",
                        "K2"),
                0
        );

        Question questionGeography2 = new Question(
                geographyCategory,
                "Which country has the largest population in the world?",
                List.of(
                        "Rusia",
                        "United States of America",
                        "China",
                        "Spain"),
                2
        );

        List<Question> geographyQuestions = List.of(questionGeography1, questionGeography2);

        // Arts
        Question questionArts1 = new Question(
                artCategory,
                "'The Phantom of the Opera' is set in which city?",
                List.of(
                        "Malaga",
                        "París",
                        "New York",
                        "London"),
                1
        );

        Question questionArts2 = new Question(
                artCategory,
                "Which famous artist is known for creating the 'Campbell's Soup Cans' artwork?",
                List.of(
                        "Vincent Van Gogh",
                        "Pablo Picasso",
                        "Andy Warhol",
                        "Claude Monet"),
                2
        );

        List<Question> artQuestions = List.of(questionArts1, questionArts2);

        // History
        Question questionHistory1 = new Question(
                historyCategory,
                "How many years did the 100 years war last?",
                List.of(
                        "100 years",
                        "101 years",
                        "99 years",
                        "116 years"),
                3
        );

        Question questionHistory2 = new Question(
                historyCategory,
                "which year Second World War started?",
                List.of(
                        "1942",
                        "1940",
                        "1939",
                        "1945"),
                2
        );

        List<Question> historyQuestions = List.of(questionHistory1, questionHistory2);

        // Entertainment
        Question questionEntertainment1 = new Question(
                entertainmentCategory,
                "What was the first feature-length animated movie ever released?",
                List.of(
                        "Toy Story",
                        "Snow White and the Seven Dwarfs",
                        "Bambi",
                        "Fantasia"),
                1
        );

        Question questionEntertainment2 = new Question(
                entertainmentCategory,
                "In The Matrix, what color is the pill Neo take?",
                List.of(
                        "Blue",
                        "Red",
                        "Black",
                        "White"),
                1
        );

        List<Question> entertainmentQuestions = List.of(questionEntertainment1, questionEntertainment2);

        // Science
        Question questionScience1 = new Question(
                scienceCategory,
                "True or False? Electrons are smaller than atoms.",
                List.of(
                        "False",
                        "True"),
                1
        );

        Question questionScience2 = new Question(
                scienceCategory,
                "Which is the most abundant element in the universe?",
                List.of(
                        "Oxigen",
                        "Helio",
                        "Hydrogen",
                        "Nitrogen"),
                2
        );

        List<Question> scienceQuestions = List.of(questionScience1, questionScience2);

        // Sports
        Question questionSport1 = new Question(
                sportsCategory,
                "What sport is best known as the ‘king of sports’?",
                List.of(
                        "Basketball",
                        "Soccer",
                        "Baseball",
                        "Chess"),
                1
        );

        Question questionSport2 = new Question(
                sportsCategory,
                "The Olympics are held every how many years?",
                List.of(
                        "Every year",
                        "Every leap year",
                        "Every 4 years",
                        "The Olympics were held just once in the history."),
                2
        );

        List<Question> sportQuestions = List.of(questionSport1, questionSport2);

        // Map
        Map<Category, List<Question>> categoryQuestionsMap = Map.of(
                geographyCategory, geographyQuestions,
                artCategory, artQuestions,
                historyCategory, historyQuestions,
                entertainmentCategory, entertainmentQuestions,
                scienceCategory, scienceQuestions,
                sportsCategory, sportQuestions
        );

        return categoryQuestionsMap;
    }

}