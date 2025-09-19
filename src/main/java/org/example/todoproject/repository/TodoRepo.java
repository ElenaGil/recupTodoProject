package org.example.todoproject.repository;

import org.example.todoproject.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepo extends MongoRepository<Todo, String> {
    Todo getTodoById(String id);
}
