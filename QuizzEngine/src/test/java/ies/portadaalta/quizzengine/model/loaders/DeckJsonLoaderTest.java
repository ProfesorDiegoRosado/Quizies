package ies.portadaalta.quizzengine.model.loaders;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckJsonLoaderTest {

    private static final String JSON_STRING_EXAMPLE = """
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

    @Test
    void loadFromString() throws IOException {

        DeckJsonLoader deckJsonLoader = new DeckJsonLoader();
        Deck deck = deckJsonLoader.loadFrom("Dummy test deck", JSON_STRING_EXAMPLE);

        Set<Category> categories = deck.getCategories();

        assertTrue(!categories.isEmpty());
        assertTrue(categories.size()==2);
        List<String> categoryNames = categories.stream().map(c -> c.getName()).toList();
        assertTrue(categoryNames.contains("Geografía"));
        assertTrue(categoryNames.contains("Entretenimiento"));

    }


    //@Test
    void loadFromFile() {
    }

}