package org.example.todoproject.model;

import java.util.List;

public record AiResponse(
        String id,
        String model,
        List<AiChoice> choices) {
}
