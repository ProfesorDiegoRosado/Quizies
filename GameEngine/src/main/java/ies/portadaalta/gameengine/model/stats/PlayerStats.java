package ies.portadaalta.gameengine.model.stats;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Question;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

    //public final int RIGHT_ANSWER_PER_CATEGORY_FOR_WINNING = 1;

    /*

    private Map<Category, CategoryStats> categoryStatsMap;

    public PlayerStats(Collection<Category> categories) {
        categoryStatsMap = new HashMap<>();
        // init categories
        for (Category category: categories) {
            categoryStatsMap.put(category, new CategoryStats());
        }
    }

    public void update(Question question, int choice) {
        Category category = question.getCategory();
        CategoryStats categoryStats = categoryStatsMap.getOrDefault(category, new CategoryStats());
        categoryStats.incNumberOfQuestions();
        if (question.isValidAnswer(choice)) {
            categoryStats.incRightAnswered();
        }
        categoryStatsMap.put(category, categoryStats);
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
     */

}
