package ies.portadata.WebServer.stomp.controllers;

import ies.portadaalta.gameengine.model.GameEngine;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;
import ies.portadata.WebServer.stomp.messages.GameEventInputMessage;
import ies.portadata.WebServer.stomp.messages.GameEventOutputMessage;
import ies.portadata.WebServer.stomp.messages.QuestionInputMessage;
import ies.portadata.WebServer.stomp.messages.StringOutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class GameEventsController {

    @Autowired
    private GameEngine gameEngine;
    private Deck deck;
    private Random rand = new Random();

    @MessageMapping("/question")
    @SendTo("/topic/question")
    public StringOutputMessage questioning(QuestionInputMessage questionInputMessage) throws Exception {
        Thread.sleep(100);
        System.out.println(questionInputMessage);
        return new StringOutputMessage(HtmlUtils.htmlEscape(questionInputMessage.toString()));
    }

    @MessageMapping("/gameevent")
    @SendTo("/topic/gameevent")
    public GameEventOutputMessage gameEvent(GameEventInputMessage gameEvent) throws Exception {
        Thread.sleep(100);
        System.out.println("GameEvent -> " + gameEvent);
        switch (gameEvent.getEvent()) {
            case "StartGame" -> {
                deck = gameEngine.getDeck();
                Set<String> categoriesNames = deck.getCategories().stream().map(Category::getName).collect(Collectors.toSet());
                return new GameEventOutputMessage("StartEvent", categoriesNames);
            }
            case "Question" -> {
                List<String> categoriesNames = gameEvent.getArguments();
                String categoryName = categoriesNames.get(rand.nextInt(categoriesNames.size()));
                Question nextQuestion = deck.getNextQuestion(categoryName);
                return new GameEventOutputMessage("Question", nextQuestion);
            }
            default -> throw new Exception("Event" + gameEvent.getEvent() + " not found");

        }
    }


}
