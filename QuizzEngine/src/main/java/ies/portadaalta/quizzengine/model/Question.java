package ies.portadaalta.quizzengine.model;

import ies.portadaalta.quizzengine.exception.InvalidRightAnswerException;

import java.util.List;

public class Question {

    private final Category category;
    private final String question;
    private final List<String> answers;
    private final int rightAnswer;

    public Question(Category category, String question, List<String> answers, int rightAnswer) {
        if (rightAnswer>=answers.size()) {
            throw new InvalidRightAnswerException(rightAnswer, answers.size());
        }
        this.category = category;
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isValidAnswer(int guess) {
        return this.rightAnswer == guess;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                ", rightAnswer=" + rightAnswer +
                '}';
    }

}
