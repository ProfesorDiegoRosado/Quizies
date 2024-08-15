package ies.portadaalta.quizzengine.model.loaders;

import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckCsvLoaderTest {

    private static final String CSV_FILENAME = "csv/ChatGPT_trivial_database.csv";
    private static final String CSV_STRING_EXAMPLE = """
            category, question, rightAnswer, answers (variable column size)
            "Geografía","¿Cuál es la capital de España?","1","Barcelona","Madrid","Valencia","Sevilla"
            "Geografía","¿En qué continente se encuentra Brasil?","0","América del Sur","Europa","África","Asia"
            "Geografía","¿Cuál es el río más largo del mundo?","0","Amazonas","Nilo","Yangtsé","Mississippi"
            "Deportes y Ocio","¿En qué deporte se utiliza una raqueta para golpear una pelota?","0","Tenis","Fútbol","Baloncesto","Golf"
            "Deportes y Ocio","¿Quién es considerado el mejor jugador de baloncesto de todos los tiempos?","0","Michael Jordan","LeBron James","Kobe Bryant","Magic Johnson"
            """;



    @Test
    void loadFromString() throws IOException {
        DeckCsvLoader deckCsvLoader = new DeckCsvLoader();
        Deck deck = deckCsvLoader.loadFromString("Dummy test deck", CSV_STRING_EXAMPLE);

        Set<Category> categories = deck.getCategories();

        assertTrue(!categories.isEmpty());
        assertTrue(categories.size()==2);
        List<String> categoryNames = categories.stream().map(c -> c.getName()).toList();
        assertTrue(categoryNames.contains("Geografía"));
        assertTrue(categoryNames.contains("Deportes y Ocio"));

    }


    @Test
    void loadFromFilename() throws IOException {
        DeckCsvLoader deckCsvLoader = new DeckCsvLoader();
        String csvFileAbsolutePath = getFileFromResources(CSV_FILENAME).getAbsolutePath();
        Deck deck = deckCsvLoader.loadFromFilename("Dummy test deck", csvFileAbsolutePath);

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

    private File getFileFromResources(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        return file;
    }

}