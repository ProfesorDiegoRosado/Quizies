package ies.portadata.WebServer.stomp.messages;

public class QuestionInputMessage {

    //private Question question;
    private String name;

    //public QuestionMessage(Question question) {
    //    this.question = question;
    //}

    public QuestionInputMessage() {}

    public QuestionInputMessage(String question) {
        this.name = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "QuestionMessage{" +
                "question=" + name +
                '}';
    }
}
