package ies.portadaalta.chatgpt;


import ies.portadaalta.chatgpt.ChatGptQuestionGenerator;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ChatGptMain {

    private static final List<String> DEFAULT_CATEGORIES = List.of(
            "Geografía",
            "Entretenimiento",
            "Historia",
            "Arte y Literatura",
            "Ciencia y Naturaleza",
            "Deportes y Ocio");

    public static void main( String[] args ) {

        ChatGptQuestionGenerator chatGptQuestionGenerator = new ChatGptQuestionGenerator(DEFAULT_CATEGORIES);

        System.out.println("Generando datos con ChatGPT ...");
        JSONArray jsonQuestionsArray = chatGptQuestionGenerator.generateJsonQuestions();

        int spacesToIndentEachLevel = 2;
        System.out.println("JSON generado:");
        System.out.println(jsonQuestionsArray.toString(spacesToIndentEachLevel));

        String jsonFilename = "ChatGPT_trivial_database.json";
        try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
            fileWriter.write(jsonQuestionsArray.toString(spacesToIndentEachLevel));
            fileWriter.flush();
            System.out.println("Archivo json %s generado con los datos de ChatGPT".formatted(jsonFilename));
        } catch (IOException e) {
            System.out.println("Error escribiendo archivo json");
        }
    }

}
