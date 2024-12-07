package ies.portadata.WebServer.stomp.controllers;

import ies.portadaalta.gameengine.model.GameEngine;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.Question;
import ies.portadata.WebServer.stomp.messages.GameEventInputMessage;
import ies.portadata.WebServer.stomp.messages.GameEventOutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


import java.util.List;
import java.util.Random;
import java.util.Set;


@Controller
public class GameEventsController {

    @Autowired
    private GameEngine gameEngine;
    private Deck deck;
    private Random rand = new Random();

    @MessageMapping("/gameevent")
    @SendTo("/topic/gameevent")
    public GameEventOutputMessage gameEvent(GameEventInputMessage gameEvent) throws Exception {
        System.out.println("GameEvent -> " + gameEvent);
        switch (gameEvent.getEvent()) {
            case "StartGame" -> {
                deck = gameEngine.getDeck();
                Set<Category> categories = deck.getCategories();
                return new GameEventOutputMessage("StartEvent", categories);
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
