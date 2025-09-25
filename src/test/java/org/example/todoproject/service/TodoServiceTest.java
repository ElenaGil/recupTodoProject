package org.example.todoproject.service;

import org.example.todoproject.TodoStatus;
import org.example.todoproject.dto.TodoDto;
import org.example.todoproject.model.Todo;
import org.example.todoproject.repository.TodoRepo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {
    TodoRepo todoRepo = mock(TodoRepo.class);
    IdService idService = mock(IdService.class);
    ChatGPTService chatGPTService = mock(ChatGPTService.class);

    TodoService todoService = new TodoService(todoRepo, idService, chatGPTService);

    Todo todo = new Todo("1", "testDescription", TodoStatus.OPEN);
    TodoDto dto = new TodoDto("testDescription", TodoStatus.OPEN);

    @Test
    void createTodo_shouldCreateNewTodo_whenCalled() {
        //GIVEN
        when(idService.randomID()).thenReturn("1");
        when(todoRepo.save(todo)).thenReturn(todo);
        when(chatGPTService.checkTextForMistakes(todo.description())).thenReturn("test-description");

        //WHEN
        Todo actual = todoService.createTodo(dto);

        //THEN
        assertEquals(todo.withDescription("test-description"), actual);
        verify(todoRepo).save(todo.withDescription("test-description"));
        verify(idService).randomID();
    }

    @Test
    void getAllTodos_shouldReturnListOfTodos() {
        //GIVEN
        when(todoRepo.findAll()).thenReturn(List.of(todo));

        //WHEN
        List<Todo> actual = todoService.getAllTodos();

        //THEN
        assertFalse(actual.isEmpty());
        assertEquals(List.of(todo), actual);
        verify(todoRepo).findAll();
    }

    @Test
    void updateTodo_shouldSaveNewDataForGivenTodo() {
        //GIVEN
        TodoDto dto = new TodoDto("newDescription", TodoStatus.IN_PROGRESS);
        Todo updatedTodo = new Todo("1", dto.description(), dto.status());

        when(todoRepo.findById("1")).thenReturn(Optional.ofNullable(todo));
        when(todoRepo.save(todo)).thenReturn(updatedTodo);

        //WHEN
        Todo actual = todoService.updateTodo("1", dto);

        //THEN
        verify(todoRepo).findById("1");
        verify(todoRepo).save(updatedTodo);
        assertEquals(actual.description(), dto.description());
        assertEquals(actual.status(), dto.status());
    }

    @Test
    void getDetailsById_shouldReturnTodoDetails_whenCalledWithExistingId() {
        //GIVEN
        when(todoRepo.findById("1")).thenReturn(Optional.ofNullable(todo));

        //WHEN
        Todo actual = todoService.findById("1");

        //THEN
        assertEquals("testDescription", actual.description());
        assertEquals(todo, actual);
        verify(todoRepo).findById("1");
    }

    @Test
    void getDetailsById_shouldThrowException_whenCalledWithNotExistingId() {
        //GIVEN
        when(todoRepo.findById("1")).thenReturn(Optional.empty());

        //WHEN
        assertThrows(NoSuchElementException.class, () -> todoService.findById("1"));

        //THEN
        verify(todoRepo).findById("1");
    }

    @Test
    void deleteTodoById() {
        //GIVEN
        doNothing().when(todoRepo).deleteById("1"); // можно и не писать эту строку

        //WHEN
        todoService.deleteTodoById("1");

        //THEN
        verify(todoRepo).deleteById("1");
    }
}