package ies.portadata.WebServer.stomp.messages;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Question;

import java.util.Set;

public class GameEventOutputMessage {

    private String type;
    private Set<Category> categories;
    private Question question;

    public GameEventOutputMessage() {}

    public GameEventOutputMessage(String type, Set<Category> categories) {
        this.type = "StartGame";
        this.categories = categories;
    }

    public GameEventOutputMessage(String type, Question question) {
        this.type = "Question";
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "GameEventOutputMessage{" +
                "type='" + type + '\'' +
                ", categories=" + categories +
                ", question=" + question +
                '}';
    }

}
