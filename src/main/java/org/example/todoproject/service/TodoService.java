package org.example.todoproject.service;

import org.example.todoproject.dto.TodoDto;
import org.example.todoproject.model.Todo;
import org.example.todoproject.repository.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {
    private final TodoRepo todoRepo;
    private final IdService idService;
    private final ChatGPTService chatGPTService;

    public TodoService(TodoRepo todoRepo, IdService idService, ChatGPTService chatGPTService) {
        this.todoRepo = todoRepo;
        this.idService = idService;
        this.chatGPTService = chatGPTService;
    }

    public Todo createTodo(TodoDto todoDto) {
        String description = chatGPTService.checkTextForMistakes(todoDto.description());
        Todo newTodo = new Todo(
                idService.randomID(),
                description,
                todoDto.status()
        );
        todoRepo.save(newTodo);

        return newTodo;
    }

    public List<Todo> getAllTodos() {
       return todoRepo.findAll();
    }

    public Todo findById(String id) {
        return todoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Todo with id: " + id + " not found!"));
    }

    public Todo updateTodo(String id, TodoDto newTodoData) {
        Todo existingTodo = todoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Todo with id: " + id + " not found!"));

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
