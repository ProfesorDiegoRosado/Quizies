package ies.portadaalta.quizzengine.model.converters;


import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;

import java.io.IOException;

public class MainJson2XmlConverter {

    public static void main( String[] args ) throws IOException {

        String jsonFilename = "src/main/resources/ChatGPT_trivial_database.json";
        String xmlFilename = "ChatGPT_trivial_database.xml";

        DeckJsonLoader jsonLoader = new DeckJsonLoader();
        Deck deck = jsonLoader.loadFromFilename("Dummy Deck", jsonFilename);

        DeckJsonConverter deckJsonConverter = new DeckJsonConverter();
        deckJsonConverter.write2Xml(deck, xmlFilename);
    }

}

