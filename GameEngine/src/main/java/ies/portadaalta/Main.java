package ies.portadaalta;


import ies.portadaalta.gameengine.model.GameEngine;
import ies.portadaalta.gameengine.model.GameEngineClient;
import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {



    public static void main(String[] args ) throws IOException {
        Main main = new Main();
        main.runGame();
    }

    private void runGame() throws IOException {
        String gameName = "Game test";

        Deck deck = createQuestionsDeck("Deck - GameEngine Main test");
        List<Player> players  = createPlayers(deck);
        GameEngine gameEngine = new GameEngine(gameName, deck, players);

        GameEngineClient gameEngineClient = new GameEngineClient();
        gameEngine.register(gameEngineClient);

        gameEngine.play();
    }

    private List<Player> createPlayers(Deck deck) {

        List<Player> players = new ArrayList<>();

        Set<Category> categories = deck.getCategories();
        Player player1 = new Player("Player 1", categories);
        Player player2 = new Player("Player 2", categories);

        players.add(player1);
        players.add(player2);

        return players;
    }

    private Deck createQuestionsDeck(String name) throws IOException {
        String JSON_STRING_EXAMPLE = """
            [
              {
                "questions": [
                  {
                    "question": {
                      "rightAnswer": 1,
                      "question": "¿Cuál es la capital de España?",
                      "answers": [
                        "Barcelona",
                        "Madrid",
                        "Valencia",
                        "Sevilla"
                      ]
                    }
                  },
                  {
                    "question": {
                      "rightAnswer": 1,
                      "question": "¿En qué país se encuentra la Torre Eiffel?",
                      "answers": [
                        "Italia",
                        "Francia",
                        "Alemania",
                        "España"
                      ]
                    }
                  }
                ],
                "category": "Geografía"
              },
              {
                "questions": [
                  {
                    "question": {
                      "rightAnswer": 0,
                      "question": "¿Cuál de las siguientes películas de Pixar se estrenó primero?",
                      "answers": [
                        "Toy Story",
                        "Buscando a Nemo",
                        "Los Increíbles",
                        "Up"
                      ]
                    }
                  },
                  {
                    "question": {
                      "rightAnswer": 0,
                      "question": "¿Quién es el protagonista de la serie de TV 'Breaking Bad'?",
                      "answers": [
                        "Walter White",
                        "Jesse Pinkman",
                        "Saul Goodman",
                        "Skyler White"
                      ]
                    }
                  }
                ],
                "category": "Entretenimiento"
              }
            ]
            """;

        DeckJsonLoader deckJsonLoader = new DeckJsonLoader();
        Deck deck = deckJsonLoader.loadFromString(name, JSON_STRING_EXAMPLE);

        return deck;
    }

}
