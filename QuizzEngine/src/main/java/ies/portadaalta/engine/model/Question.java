package ies.portadaalta.engine.model;

import ies.portadaalta.engine.exception.InvalidRightAnswerException;

import java.util.List;

public class Question {

    private String question;
    private List<String> answers;
    private int rightAnswer;

    public Question(String question, List<String> answers, int rightAnswer) {
        if (rightAnswer>=answers.size()) {
            throw new InvalidRightAnswerException(rightAnswer, answers.size());
        }
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
