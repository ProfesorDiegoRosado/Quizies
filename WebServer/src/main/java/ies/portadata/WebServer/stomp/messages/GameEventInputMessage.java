package ies.portadata.WebServer.stomp.messages;

import java.util.ArrayList;
import java.util.List;

public class GameEventInputMessage {

    private String event;
    private List<String> arguments;

    public GameEventInputMessage(){}

    public GameEventInputMessage(String event){
        this.event = event;
        this.arguments = new ArrayList<>();
    }

    public String getEvent() {
        return event;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "GameEventInputMessage{" +
                "event='" + event + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
