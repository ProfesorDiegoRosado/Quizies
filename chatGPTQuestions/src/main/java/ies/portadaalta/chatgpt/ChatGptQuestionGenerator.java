package ies.portadaalta.chatgpt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ChatGptQuestionGenerator {

    //private static final int TURBO_3_5_TOKEN_CONTENT_MAX_LENGTH = 18385;
    private static final int TURBO_3_5_TOKEN_CONTENT_MAX_LENGTH = 1000;

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

    private static final String JSON_CATEGORY_KEY = "category";

    private List<String> categories;

    public ChatGptQuestionGenerator(List<String> categories) {
        this.categories = categories;
    }

    public JSONArray generateJsonQuestions() {
        JSONArray jsonQuestionsArray = new JSONArray();
        for (String category: categories) {
            System.out.println("Generando datos para la categoria %s ...".formatted(category));
            JSONObject jsonCategoryQuestions = getCategoryQuestions(category);
            jsonQuestionsArray.put(jsonCategoryQuestions);
        }
        return jsonQuestionsArray;
    }

    private JSONObject getCategoryQuestions(String category) {
        JSONObject jsonCategoryQuestions = new JSONObject();
        jsonCategoryQuestions.put(JSON_CATEGORY_KEY, category);
        JSONArray jsonArrayCategoryQuestions = getAllQuestionsFor(category);
        jsonCategoryQuestions.put("questions", jsonArrayCategoryQuestions);
        return jsonCategoryQuestions;
    }

    private JSONArray getAllQuestionsFor(String category) {

       ChatGptClient client = new ChatGptClient();

       JSONArray jsonQuestionsArray = new JSONArray();

       try {
            boolean end = false;

            while (!end) {
                String prompt;

                if (client.firstMessage()) {
                    prompt = PROMPT.formatted(category);
                } else {
                    prompt = "another one";
                }
                ChatGptResponse chatGptResponse = client.query(prompt);
                String response = chatGptResponse.getResponse();

                //System.out.println(response);
                //System.out.println("Token context size: %d".formatted(chatGptResponse.getTotalTokens()));

                JSONObject jsonQuestionObject = extractJsonQuestionObject(response);

                jsonQuestionsArray.put(jsonQuestionObject);

                if (chatGptResponse.getTotalTokens()>TURBO_3_5_TOKEN_CONTENT_MAX_LENGTH) {
                    end = true;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       return jsonQuestionsArray;
    }

    private JSONObject extractJsonQuestionObject(String response) {
        JSONObject jsonReponseObject = new JSONObject(response);
        return jsonReponseObject;
    }

}
