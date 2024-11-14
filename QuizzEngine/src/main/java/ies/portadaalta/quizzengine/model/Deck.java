package ies.portadaalta.quizzengine.model;

import ies.portadaalta.quizzengine.exception.NoCategoryFoundException;

import java.util.*;

public class Deck {

    private final String name;
    private Map<Category, List<Question>> categoryQuestionsMap = new HashMap<>();
    private Random random = new Random();

    public Deck(String deckName,
            Category category1, List<Question> questions1,
            Category category2, List<Question> questions2,
            Category category3, List<Question> questions3,
            Category category4, List<Question> questions4,
            Category category5, List<Question> questions5,
            Category category6, List<Question> questions6) {

        this.name = deckName;
        categoryQuestionsMap.put(category1, questions1);
        categoryQuestionsMap.put(category2, questions2);
        categoryQuestionsMap.put(category3, questions3);
        categoryQuestionsMap.put(category4, questions4);
        categoryQuestionsMap.put(category5, questions5);
        categoryQuestionsMap.put(category6, questions6);
    }

    public Deck(String deckName,
                Map<Category, List<Question>> categoryQuestionsMap) {

        this(deckName, categoryQuestionsMap, new Random());
    }

    public Deck(String deckName,
                Map<Category, List<Question>> categoryQuestionsMap,
                Random random) {

        this.name = deckName;
        this.categoryQuestionsMap = categoryQuestionsMap;
        this.random = random;
    }

    public Set<Category> getCategories() {
        return categoryQuestionsMap.keySet();
    }

    public String getName() {
        return name;
    }

    public Category getNextCagetory() {
        List<Category> categories = getCategories().stream().toList();
        int categoryPosition = random.nextInt(categories.size());
        Category category = categories.get(categoryPosition);
        return category;
    }

    public Question getNextQuestion(Category category) {
        assertCategoryExists(category);
        List<Question> questions = categoryQuestionsMap.get(category);
        int questionPosition = random.nextInt(questions.size());
        Question question = questions.get(questionPosition);
        return question;
    }

    public Question getNextQuestion(String categoryName) {
        Category category = getCategories().stream().
                filter(
                        c -> c.getName().equalsIgnoreCase(categoryName)
                ).findFirst().get();
        return getNextQuestion(category);
    }

    public void addQuestion(Question question) {
        categoryQuestionsMap.get(question.getCategory()).add(question);
    }

    public Category getCategoryWithName(String categoryName) {
        Optional<Category> optCategory = categoryQuestionsMap.keySet().stream().filter(c -> c.getName().equalsIgnoreCase(categoryName) ).findFirst();
        if (optCategory.isEmpty()) {
            throw new RuntimeException("Category " + categoryName + " not found");
        } else {
            return optCategory.get();
        }
    }

    public Question getNextRandomQuestion() {
        return getNextQuestion(getNextCagetory());
    }

    private void assertCategoryExists(Category category) {
        if (!categoryQuestionsMap.containsKey(category)) {
            throw new NoCategoryFoundException(category);
        }
    }

    public List<Question> getQuestionsForCategory(Category category) {
        return categoryQuestionsMap.get(category);
    }

}
