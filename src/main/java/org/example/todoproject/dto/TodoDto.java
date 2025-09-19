package org.example.todoproject.dto;

import lombok.With;
import org.example.todoproject.TodoStatus;

@With
public record TodoDto(String description, TodoStatus status) {
}
