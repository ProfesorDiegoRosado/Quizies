package ies.portadaalta.mistralai;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MistralAiClient {

    private static final String MISTRALAI_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String MODEL = "mistral-large-latest";
    private static final String MISTRALAI_SECRET_API_KEY = System.getenv("MISTRALAI_SECRET_API_KEY");

    private static final String USER_ROLE = "user";
    private static final String ASISTANT_ROLE = "assistant";

    private JSONArray messagesArray;

    public MistralAiClient() {
        messagesArray = new JSONArray();
    }

    public MistralAiResponse query(String prompt) throws IOException {

        HttpURLConnection con = getHttpURLConnection();

        JSONObject messageParameters = getMessageParameters();

        addMessageToContext(USER_ROLE, prompt);

        con.setDoOutput(true);
        con.getOutputStream().write(messageParameters.toString().getBytes());

        /*
        InputStream is;
        if (con.getResponseCode() >= 400) {
            is = con.getErrorStream();
        } else {
            is = con.getInputStream();
        }
         */

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        MistralAiResponse mistralAiResponse = new MistralAiResponse(output);

        // Custom for this example where I want to optimize chat context (messages history).
        JSONObject jsonResponseObject = new JSONObject(mistralAiResponse.getResponse());
        //System.out.println("----- mistralAiResponse.getResponse() -----");
        //System.out.println(mistralAiResponse.getResponse());
        if (validJsonResponse(jsonResponseObject)) {
            String question = jsonResponseObject.getJSONObject("question").getString("question");
            addMessageToContext(ASISTANT_ROLE, question);
        } else {
            // No valid json response
            mistralAiResponse = null;
        }

        return mistralAiResponse;
    }

    private boolean validJsonResponse(JSONObject jsonResponseObject) {
        return jsonResponseObject.has("question") &&
                jsonResponseObject.getJSONObject("question").has("question");
    }

    private static HttpURLConnection getHttpURLConnection() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(MISTRALAI_URL).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + MISTRALAI_SECRET_API_KEY);
        return con;
    }

    private JSONObject getMessageParameters() {
        JSONObject responseJsonMode = new JSONObject();
        responseJsonMode.put("type", "json_object");

        JSONObject data = new JSONObject();
        data.put("model", MODEL);
        data.put("messages", messagesArray);
        data.put("response_format", responseJsonMode);
        //data.put("temperature", 1.0);
        //data.put("top_p", 1.0);
        //data.put("n", 1); // number of choices in the response
        return data;
    }

    private void addMessageToContext(String role, String content) {
        JSONObject messageAnotherOneObject = new JSONObject();
        messageAnotherOneObject.put("role", role);
        messageAnotherOneObject.put("content", content);
        messagesArray.put(messageAnotherOneObject);
    }

    public boolean firstMessage() {
        return messagesArray.length()==0;
    }

}
