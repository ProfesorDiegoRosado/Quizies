package ies.portadaalta.quizzengine.model.converters;

import java.io.IOException;


/*
 Create a csv file questions database from json input file.
 */
public class MainJson2CsvConverter {

    public static void main( String[] args ) throws IOException {

        String jsonFilename = "src/main/resources/ChatGPT_trivial_database.json"; //"ChatGPT_trivial_database.json";
        String csvFilename = "ChatGPT_trivial_database.csv";

        DeckJsonConverter deckJsonConverter = new DeckJsonConverter();

        deckJsonConverter.convertoJson2Csv(jsonFilename, csvFilename);

    }


}
