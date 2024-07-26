package ies.portadaalta;

import ies.portadaalta.chatgpt.ChatGptClient;
import ies.portadaalta.chatgpt.ChatGptResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final int TURBO_3_5_TOKEN_CONTENT_MAX_LENGTH = 18385;

    /*
    // With json example
    private static final String PROMPT = """
            I'm working on a Trivial software project. The main language of the project is going to be Spanish. I would like you to help me to generate some questions in Spanish but the source code and the json fields are going to be in English.
            The output format must be a valid json to be consumed by the Trivial project.
            The expected output must have the following fields:
             * category: one of the following
                 - "Geografía"
                 - "Entretenimiento"
                 - "Historia"
                 - "Arte y Literatura"
                 - "Ciencia y Naturaleza"
                 - "Deportes y Ocio"
             * question: an object with the following fields:
                 - "question": the actual question to be answered.
                 - "answers": an array with the possible answers. 4 answers are expected but just one is the right one.
                 - "rightAnswer": an integer pointing the single right answer of the "answers" array where index starts at 0.
            Below, between three dashes ( --- ), you can find an example of the expected json.
            Please, do not repeat questions.
                        
            ---
            {
                "category": "Geografía",
                "question": {
                    "question": "¿Cuál es el pico más alto de España?",
                    "answers": {
                        "Aneto",
                        "Veleta",
                        "Teide",
                        "Mulhacén"
                    },
                    "rightAnswer": 2
                }
            }
            ---
                        
            Could you generate an example for the "Geografía" category.
            """;

     */

    /*
    private static final String PROMPT = """
            I'm working on a Trivial software project. The main language of the project is going to be Spanish. I would like you to help me to generate some questions in Spanish but the source code and the json fields are going to be in English.
            The output format must be a valid json to be consumed by the Trivial project.
            The expected output must have the following fields:
             * category: one of the following
                 - "Geografía"
                 - "Entretenimiento"
                 - "Historia"
                 - "Arte y Literatura"
                 - "Ciencia y Naturaleza"
                 - "Deportes y Ocio"
             * question: an object with the following fields:
                 - "question": the actual question to be answered.
                 - "answers": an array with the possible answers. 4 answers are expected but just one is the right one.
                 - "rightAnswer": an integer pointing the single right answer of the "answers" array where index starts at 0.

            Please, do not repeat questions.
                                                
            Could you generate an example for the "Geografía" category.
            """;
    */

    // Remove category from json response
    private static final String PROMPT = """
            I'm working on a Trivial software project. The main language of the project is going to be Spanish. I would like you to help me to generate some questions in Spanish but the source code and the json fields are going to be in English.
            The output format must be a valid json to be consumed by the Trivial project.
            The expected output must have the following fields:
             * question: an object with the following fields:
                 - "question": the actual question to be answered.
                 - "answers": an array with the possible answers. 4 answers are expected but just one is the right one.
                 - "rightAnswer": an integer pointing the single right answer of the "answers" array where index starts at 0.

            Please, do not repeat questions.
                                                
            Could you generate an example for the "%s" category.
            """;

    public static void main( String[] args ) {

        ChatGptClient client = new ChatGptClient();

        Scanner scanner = new Scanner(System.in);

        try {
            boolean end = false;

            while (!end) {
                String prompt;

                if (client.firstMessage()) {
                    prompt = PROMPT.formatted("Geografía");
                } else {
                    prompt = "another one";
                }
                ChatGptResponse chatGptResponse = client.query(prompt);
                String response = chatGptResponse.getResponse();

                System.out.println(response);

                System.out.println("Intro para continuar ... n para terminar");
                String another = scanner.nextLine();
                if (another=="n") {
                    end = true;
                }
                if (chatGptResponse.getTotalTokens()>TURBO_3_5_TOKEN_CONTENT_MAX_LENGTH) {
                    end = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
