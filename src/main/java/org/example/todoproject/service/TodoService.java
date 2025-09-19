package org.example.todoproject.service;

import org.example.todoproject.TodoStatus;
import org.example.todoproject.model.Todo;
import org.example.todoproject.repository.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepo todoRepo;

    public TodoService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    public Todo createTodo(String todoDescription) {
        return todoRepo.save(new Todo(null, todoDescription, TodoStatus.OPEN));
    }

    public List<Todo> getAllTodos() {
       return todoRepo.findAll();
    }

    public Todo getDetailsById(String id) {
        return todoRepo.getTodoById(id);
    }

    public Todo updateTodo(String id, Todo newTodoData) {
        Todo updatedTodo = todoRepo.getTodoById(id)
                .withStatus(newTodoData.status())
                .withDescription(newTodoData.description());
        return todoRepo.save(updatedTodo);
    }

    public void deleteTodoById(String id) {
        todoRepo.deleteById(id);
    }
}
