package ies.portadaalta.quizzengine.model.loaders;

import ies.portadaalta.quizzengine.TestUtils;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckSQLiteLoaderTest {

    private static final String DB_NAME = "SQLite/ChatGPT_trivial_database.db";

    @Test
    void loadFromDb() throws SQLException {
        DeckSQLiteLoader deckSQLiteLoader = new DeckSQLiteLoader();
        String dbNameAbsolutePath = new TestUtils().getFileFromResources(DB_NAME).getAbsolutePath();
        Deck deck = deckSQLiteLoader.loadFromDb("Deck from SQLite", dbNameAbsolutePath);

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