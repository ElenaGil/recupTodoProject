package org.example.todoproject.service;

import org.example.todoproject.dto.TodoDto;
import org.example.todoproject.model.Todo;
import org.example.todoproject.repository.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepo todoRepo;
    private final IdService idService;

    public TodoService(TodoRepo todoRepo, IdService idService) {
        this.todoRepo = todoRepo;
        this.idService = idService;
    }

    public Todo createTodo(TodoDto todoDescription) {
        Todo newTodo = new Todo(idService.randomID(), todoDescription.description(), todoDescription.status());
        todoRepo.save(newTodo);

        return newTodo;
    }

    public List<Todo> getAllTodos() {
       return todoRepo.findAll();
    }

    public Todo findById(String id) {
        return todoRepo.findById(id).orElseThrow();
    }

    public Todo updateTodo(String id, TodoDto newTodoData) {
        Todo existingTodo = todoRepo.findById(id).orElseThrow();

        Todo updatedTodo = existingTodo
                .withStatus(newTodoData.status())
                .withDescription(newTodoData.description());

        todoRepo.save(updatedTodo);

        return updatedTodo;
    }

    public void deleteTodoById(String id) {
        todoRepo.deleteById(id);
    }
}
