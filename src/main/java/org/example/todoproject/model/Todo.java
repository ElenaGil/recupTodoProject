package org.example.todoproject.model;

import lombok.With;
import org.example.todoproject.TodoStatus;

@With
public record Todo(String id, String description, TodoStatus status) {
}
