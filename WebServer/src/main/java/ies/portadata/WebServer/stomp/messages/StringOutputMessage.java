package ies.portadata.WebServer.stomp.messages;

public class StringOutputMessage {

    private String questioning;

    public StringOutputMessage() {}

    public StringOutputMessage(String questioning) {
        this.questioning = questioning;
    }

    public String getQuestioning() {
        return questioning;
    }

    @Override
    public String toString() {
        return "Questioning{" +
                "questioning='" + questioning + '\'' +
                '}';
    }

}
