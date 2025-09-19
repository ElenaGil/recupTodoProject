package org.example.todoproject.controller;

import org.example.todoproject.model.Todo;
import org.example.todoproject.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    public Todo createTodo(@RequestBody String todoDescription) {
        return todoService.createTodo(todoDescription);
    }

    @GetMapping()
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getDetailsById(@PathVariable String id) {
        return todoService.getDetailsById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

    @PutMapping("/{id}")
    public Todo editById(@PathVariable String id, @RequestBody Todo newTodoData) {
        return todoService.updateTodo(id, newTodoData);
    }
}
