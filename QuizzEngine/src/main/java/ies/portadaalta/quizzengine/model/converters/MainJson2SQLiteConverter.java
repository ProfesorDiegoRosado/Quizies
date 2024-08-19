package ies.portadaalta.quizzengine.model.converters;

import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;

import java.io.IOException;
import java.sql.SQLException;

public class MainJson2SQLiteConverter {


    public static void main( String[] args ) throws IOException, SQLException {

        String jsonFilename = "src/main/resources/ChatGPT_trivial_database.json";
        String dbName = "ChatGPT_trivial_database.db";

        DeckJsonLoader jsonLoader = new DeckJsonLoader();
        Deck deck = jsonLoader.loadFromFilename("Dummy Deck", jsonFilename);

        DeckJsonConverter deckJsonConverter = new DeckJsonConverter();
        deckJsonConverter.writeDeck2Sqlite(deck, dbName);
    }

}
