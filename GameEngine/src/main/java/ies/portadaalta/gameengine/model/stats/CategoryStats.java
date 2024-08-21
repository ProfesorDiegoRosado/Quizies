package ies.portadaalta.gameengine.model.stats;

public class CategoryStats {

    private int numberOfQuestions = 0;
    private int rightAnswered = 0;

    public CategoryStats() {}

    public void incNumberOfQuestions() {
        numberOfQuestions++;
    }

    public void incRightAnswered() {
        rightAnswered++;
    }


    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getRightAnswered() {
        return rightAnswered;
    }

    public int getWrongAnswered() {
        return numberOfQuestions - rightAnswered;
    }

    public double getSuccessAnswerRate() {
        double rate = 0;
        if (numberOfQuestions != 0) {
            rate = (double)rightAnswered / numberOfQuestions;
        }
        return rate;
    }

    @Override
    public String toString() {
        return "CategoryStats{" +
                "numberOfQuestions=" + numberOfQuestions +
                ", rightAnswered=" + rightAnswered +
                '}';
    }
}
