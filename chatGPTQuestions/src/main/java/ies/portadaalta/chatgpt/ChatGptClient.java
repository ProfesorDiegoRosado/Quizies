package ies.portadaalta.chatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGptClient {

    //private static final String CHATGPT_URL = "https://api.openai.com/v1/completions";
    private static final String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    //private static final String MODEL = "gpt-4o-mini";
    private static final String CHATGPT_SECRET_API_KEY = System.getenv("CHATGPT_SECRET_API_KEY");

    private static final String USER_ROLE = "user";
    private static final String ASISTANT_ROLE = "assistant";

    private JSONArray messagesArray;

    public ChatGptClient() {
        messagesArray = new JSONArray();
    }

    public ChatGptResponse query(String prompt) throws IOException {

        HttpURLConnection con = getHttpURLConnection();

        addMessageToContext(USER_ROLE, prompt);

        JSONObject messageParameters = getMessageParameters();

        con.setDoOutput(true);
        con.getOutputStream().write(messageParameters.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        ChatGptResponse chatGptresponse = new ChatGptResponse(output);

        // Custom for this example where I want to optimize chat context (messages history).
        JSONObject jsonResponseObject = new JSONObject(chatGptresponse.getResponse());
        //System.out.println("----- chatGptresponse.getResponse() -----");
        //System.out.println(chatGptresponse.getResponse());
        String question = jsonResponseObject.getJSONObject("question").getString("question");

        addMessageToContext(ASISTANT_ROLE, question);

        return chatGptresponse;
    }

    private static HttpURLConnection getHttpURLConnection() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(CHATGPT_URL).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + CHATGPT_SECRET_API_KEY);
        return con;
    }

    private JSONObject getMessageParameters() {
        JSONObject responseJsonMode = new JSONObject();
        responseJsonMode.put("type", "json_object");

        JSONObject data = new JSONObject();
        data.put("model", MODEL);
        data.put("messages", messagesArray);
        data.put("response_format", responseJsonMode);
        data.put("temperature", 1.0);
        data.put("top_p", 1.0);
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
