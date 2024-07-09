package ies.portadaalta.engine.exception;

public class InvalidRightAnswerException extends RuntimeException {

    private int rightAnswer;
    private int answersSize;

    public InvalidRightAnswerException(int rightAnswer, int answersSize) {
        this.rightAnswer = rightAnswer;
        this.answersSize = answersSize;
    }

    public String toString() {
        return "RightAnswer value is " + rightAnswer + ", which is above the answersSize " + answersSize;
    }

}
