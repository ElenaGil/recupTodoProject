package org.example.todoproject.controller;

import org.example.todoproject.dto.TodoDto;
import org.example.todoproject.model.Todo;
import org.example.todoproject.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody TodoDto dto) {
        return todoService.createTodo(dto);
    }

    @GetMapping()
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return todoService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Todo editTodo(@PathVariable String id, @RequestBody TodoDto newTodoData) {
        return todoService.updateTodo(id, newTodoData);
    }

    // локальный хандлер имеет преимущество перед глобальным
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException() {
        return "Element with given ID is not found";
    }
}
