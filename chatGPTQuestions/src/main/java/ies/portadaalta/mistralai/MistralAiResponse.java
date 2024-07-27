package ies.portadaalta.mistralai;

import org.json.JSONObject;


// So far MistralAi Response is pretty similar to ChatGpt Response
public class MistralAiResponse {

    private String fullResponse;
    private String response;
    private int totalTokens;

    public MistralAiResponse(String fullResponse) {
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
        return "MistralAiResponse{" +
                "fullResponse='" + fullResponse + '\'' +
                ", response='" + response + '\'' +
                ", totalTokens=" + totalTokens +
                '}';
    }
}
