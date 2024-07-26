package ies.portadaalta.chatgpt;

import org.json.JSONObject;

public class ChatGptResponse {

    private String fullResponse;
    private String response;
    private int totalTokens;

    public ChatGptResponse(String fullResponse) {
        this.fullResponse = fullResponse;
        this.response = extractResponse();
        this.totalTokens = extractTotalTokens();
    }

    private String extractResponse() {
        return new JSONObject(fullResponse).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }

    private int extractTotalTokens() {
        return new JSONObject(fullResponse).getJSONObject("usage").getInt("total_tokens");
    }

    public String getFullResponse() {
        return fullResponse;
    }

    public String getResponse() {
        return response;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    @Override
    public String toString() {
        return "ChatGptResponse{" +
                "fullResponse='" + fullResponse + '\'' +
                ", response='" + response + '\'' +
                ", totalTokens=" + totalTokens +
                '}';
    }
}
