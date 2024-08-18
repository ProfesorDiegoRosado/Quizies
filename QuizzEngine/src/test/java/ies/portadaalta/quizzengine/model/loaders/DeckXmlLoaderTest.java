package ies.portadaalta.quizzengine.model.loaders;

import ies.portadaalta.quizzengine.TestUtils;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckXmlLoaderTest {

    private static final String XML_FILENAME = "xml/ChatGPT_trivial_database.xml";

    private static final String XML_STRING_EXAMPLE = """
            <?xml version="1.0" encoding="UTF-8"?>
            <quizies>
              <category name="Entretenimiento">
                <questions>
                  <question question="¿Cuál de las siguientes películas de Pixar se estrenó primero?">
                    <answers>
                      <answer rightAnswer="true">Toy Story</answer>
                      <answer>Buscando a Nemo</answer>
                      <answer>Los Increíbles</answer>
                      <answer>Up</answer>
                    </answers>
                  </question>
                  <question question="¿Quién es el protagonista de la serie de TV 'Breaking Bad'?">
                    <answers>
                      <answer rightAnswer="true">Walter White</answer>
                      <answer>Jesse Pinkman</answer>
                      <answer>Saul Goodman</answer>
                      <answer>Skyler White</answer>
                    </answers>
                  </question>
                  <question question="¿Cuál es el nombre del actor que interpretó a Jack Sparrow en la saga de 'Piratas del Caribe'?">
                    <answers>
                      <answer rightAnswer="true">Johnny Depp</answer>
                      <answer>Orlando Bloom</answer>
                      <answer>Brad Pitt</answer>
                      <answer>Tom Cruise</answer>
                    </answers>
                  </question>
                </questions>
              </category>
              <category name="Geografía">
                <questions>
                  <question question="¿Cuál es la capital de España?">
                    <answers>
                      <answer>Barcelona</answer>
                      <answer rightAnswer="true">Madrid</answer>
                      <answer>Valencia</answer>
                      <answer>Sevilla</answer>
                    </answers>
                  </question>
                  <question question="¿En qué continente se encuentra Brasil?">
                    <answers>
                      <answer rightAnswer="true">América del Sur</answer>
                      <answer>Europa</answer>
                      <answer>África</answer>
                      <answer>Asia</answer>
                    </answers>
                  </question>
                </questions>
              </category>
            </quizies>
            """;

    @Test
    void loadFromString() throws IOException, JDOMException {
        DeckXmlLoader deckXmlLoader = new DeckXmlLoader();
        Deck deck = deckXmlLoader.loadFromString("Dummy test deck", XML_STRING_EXAMPLE);

        Set<Category> categories = deck.getCategories();

        assertTrue(!categories.isEmpty());
        assertTrue(categories.size()==2);
        List<String> categoryNames = categories.stream().map(c -> c.getName()).toList();
        assertTrue(categoryNames.contains("Geografía"));
        assertTrue(categoryNames.contains("Entretenimiento"));
    }

    @Test
    void loadFromFilename() throws IOException, JDOMException {

        DeckXmlLoader deckXmlLoader = new DeckXmlLoader();
        String xmlFileAbsolutePath = new TestUtils().getFileFromResources(XML_FILENAME).getAbsolutePath();

        Deck deck = deckXmlLoader.loadFromFilename("Dummy test deck", xmlFileAbsolutePath);

        Set<Category> categories = deck.getCategories();

        assertTrue(!categories.isEmpty());
        assertTrue(categories.size()==6);
        List<String> categoryNames = categories.stream().map(c -> c.getName()).toList();
        categoryNames.containsAll(
                List.of("Geografía",
                        "Entretenimiento",
                        "Historia",
                        "Ciencia y Naturaleza",
                        "Deportes y Ocio",
                        "Arte y Literatura")
        );

    }

}