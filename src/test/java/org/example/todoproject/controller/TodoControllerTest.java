package org.example.todoproject.controller;

import org.example.todoproject.TodoStatus;
import org.example.todoproject.model.Todo;
import org.example.todoproject.repository.TodoRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
// вместо BeforeEach и AfterEach в моем случае можно использовать @DirtiesContext
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodoRepo todoRepo;

    @Autowired
    MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        Todo todo = new Todo("1", "test-TODO", TodoStatus.OPEN);
        todoRepo.save(todo);
    }

    @AfterEach
    void tearDown() {
        todoRepo.deleteAll();
    }

    @Test
    void createTodo_shouldReturnCreatedTodo() throws Exception {
        //!!! в папке test в application.properties нужно ОБЯЗАТЕЛЬНО указать мнимый OPENAI_API_KEY !!!
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("""
                    {
                      "model": "gpt-5",
                      "messages": [
                        {
                          "role": "user",
                          "content": "Give me the following text without mistakes: NEWW TODO"
                        }
                      ]
                    }
                    """))
                .andRespond(withSuccess("""
                    {
                        "id": "12345",
                        "model": "gpt-5",
                        "choices": [
                            {
                                "message": {
                                    "content": "NEW TODO",
                                    "role": "user"
                                }
                            }
                        ]
                    }
                    """, MediaType.APPLICATION_JSON));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "description": "NEWW TODO",
                                "status": "IN_PROGRESS"
                            }
                        """)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                  {
                     "description": "NEW TODO",
                     "status": "IN_PROGRESS"
                  }
                """));
    }

    @Test
    void getAllTodos_shouldReturnListWithOneTodo() throws Exception {

        mockMvc.perform((MockMvcRequestBuilders.get("/api/todo")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                  [
                      {
                         "description": "test-TODO",
                         "status": "OPEN"
                      }
                  ]
                """));
    }

    @Test
    void getTodoById_shouldReturnTodoDataWithGivenId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                      {
                         "description": "test-TODO",
                         "status": "OPEN"
                      }
                """));
    }

    @Test
    void deleteTodo_shouldRemoveCreatedTodo() throws Exception {
        int numberOfTodos = todoRepo.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(numberOfTodos - 1, todoRepo.findAll().size());
    }

    @Test
    void editTodo_shouldSaveChangedDescriptionAndStatus_whenCalledWithExistingId() throws Exception {
        String todoDto = """
                        {
                          "description": "Updated TODO",
                          "status": "DONE"
                        }
                        """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDto))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(todoDto));
    }

    @Test
    void editTodo_shouldThrowErrorStatus_whenCalledWithNotExistingId() throws Exception {
        String todoDto = """
                        {
                          "description": "Updated TODO",
                          "status": "DONE"
                        }
                        """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDto))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Element with given ID is not found"));
    }
}