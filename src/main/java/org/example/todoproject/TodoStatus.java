package org.example.todoproject;

import lombok.Getter;

@Getter
public enum TodoStatus {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String value;

    TodoStatus(String value) {
        this.value = value;
    }

}
