package org.example.todoproject.service;

import org.example.todoproject.model.AiMessage;
import org.example.todoproject.model.AiRequest;
import org.example.todoproject.model.AiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(
            RestClient.Builder restClientBuilder,
            @Value("${OPENAI_API_KEY}") String apiKey
    ) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String checkTextForMistakes(String text) {
        List<AiMessage> messageList = new ArrayList<>(
                List.of(new AiMessage("user", "Give me this text without mistakes: " + text)));
        AiRequest request  = new AiRequest("gpt-5", messageList);

        AiResponse aiResponse = restClient.post()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AiResponse.class);
        try {
            return aiResponse.choices().get(0).message().content();
        } catch (NullPointerException exception) {
            return "No data in response";
        }

    }
}
