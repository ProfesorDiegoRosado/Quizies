package ies.portadaalta.gameengine.model;

import ies.portadaalta.gameengine.model.stats.CategoryStats;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Question;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player implements ChoiceProvider {

    public final int RIGHT_ANSWER_PER_CATEGORY_FOR_WINNING = 1;

    private final String name;
    private Map<Category, CategoryStats> categoryStatsMap;

    public Player(String name, Collection<Category> categories) {
        this.name = name;
        this.categoryStatsMap = initCategoryStats(categories);
    }

    private Map<Category, CategoryStats> initCategoryStats(Collection<Category> categories) {
        Map<Category, CategoryStats> localMap = new HashMap<>();
        // init categories
        for (Category category: categories) {
            localMap.put(category, new CategoryStats());
        }
        return localMap;
    }

    public String getName() {
        return name;
    }

    public void updateStats(Question question, int choice) {
        Category category = question.getCategory();
        CategoryStats categoryStats = categoryStatsMap.get(category);
        assert (categoryStats!=null);

        categoryStats.incNumberOfQuestions();
        if (question.isValidAnswer(choice)) {
            categoryStats.incRightAnswered();
        }
    }

    public Map<Category, CategoryStats> getCategoryStats() {
        return categoryStatsMap;
    }

    public boolean isWinner() {
        boolean isWinner = true;
        for (Category category: categoryStatsMap.keySet()) {
            if ( categoryStatsMap.get(category).getRightAnswered() < RIGHT_ANSWER_PER_CATEGORY_FOR_WINNING ) {
                isWinner = false;
                break;
            }
        }
        return isWinner;
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    // from ChoiceProvider interface
    @Override
    public int getChoice() {
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        return i-1;
    }
}
