package com.fcai.SoftwareTesting;

import com.fcai.SoftwareTesting.todo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SoftwareTestingApplicationTests {

    @Autowired
    private TodoController controller;

    /**
     * This method is used to initialize the todoService object
     * its purpose is to reset the todos list in order to test the methods independently
     */
    private void init() {
        controller.resetTodoService();
    }

    @Test
    void testCreateMethodNull() {
        init();
        ResponseEntity<Todo> todo = controller.create(null);
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testCreateMethodEmptyTitle() {
        init();
        TodoCreateRequest todoRequest = new TodoCreateRequest();
        todoRequest.setTitle("");
        ResponseEntity<Todo> todo = controller.create(todoRequest);
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testCreateMethodEmptyDescription() {
        init();
        TodoCreateRequest todoRequest = new TodoCreateRequest();
        todoRequest.setTitle("test");
        todoRequest.setDescription("");
        ResponseEntity<Todo> todo = controller.create(todoRequest);
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testCreateMethodExpectedReturn() {
        init();
        TodoCreateRequest todoRequest = new TodoCreateRequest("test todo", "test description");
        Todo expectedTodo = new Todo(
                "1",
                "test todo",
                "test description",
                false
        );
        ResponseEntity<Todo> todo = controller.create(todoRequest);
        assertEquals(expectedTodo.getId(), todo.getBody().getId());
        assertEquals(expectedTodo.getTitle(), todo.getBody().getTitle());
        assertEquals(expectedTodo.getDescription(), todo.getBody().getDescription());
        assertEquals(expectedTodo.isCompleted(), todo.getBody().isCompleted());

    }

    @Test
    void testReadMethodNull() {
        init();
        ResponseEntity<Todo> todo = controller.read(null);
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testReadMethodEmpty() {
        init();
        ResponseEntity<Todo> todo = controller.read("");
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testReadMethodIDNotFoundNotInList() {
        init();
        ResponseEntity<Todo> todo = controller.read("1");
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testReadMethodIDInList() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        ResponseEntity<Todo> todoResponse = controller.create(todo);
        Todo expectedTodo = new Todo(
                "1",
                "test todo",
                "test description",
                false
        );
        ResponseEntity<Todo> actualTodoResponse = controller.read("1");
        assertEquals(expectedTodo.getId(), actualTodoResponse.getBody().getId());
        assertEquals(expectedTodo.getTitle(), actualTodoResponse.getBody().getTitle());
        assertEquals(expectedTodo.getDescription(), actualTodoResponse.getBody().getDescription());
        assertEquals(expectedTodo.isCompleted(), actualTodoResponse.getBody().isCompleted());
    }

    @Test
    void testUpdateMethodNotInList() {
        init();
        ResponseEntity<Todo> todo = controller.update("1", true);
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testUpdateMethodInListTrue() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        controller.create(todo);
        Todo tempTodo = new Todo(
                "1",
                "test todo",
                "test description",
                false
        );
        ResponseEntity<Todo> actualTodoResponse = controller.update("1", true);
        assertEquals(tempTodo.getId(), actualTodoResponse.getBody().getId());
        assertEquals(tempTodo.getTitle(), actualTodoResponse.getBody().getTitle());
        assertEquals(tempTodo.getDescription(), actualTodoResponse.getBody().getDescription());
        assertTrue(actualTodoResponse.getBody().isCompleted());
    }

    @Test
    void testUpdateMethodInListFalse() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        controller.create(todo);
        Todo tempTodo = new Todo(
                "1",
                "test todo",
                "test description",
                false
        );
        ResponseEntity<Todo> actualTodoResponse = controller.update("1", false);
        assertEquals(tempTodo.getId(), actualTodoResponse.getBody().getId());
        assertEquals(tempTodo.getTitle(), actualTodoResponse.getBody().getTitle());
        assertEquals(tempTodo.getDescription(), actualTodoResponse.getBody().getDescription());
        assertFalse(actualTodoResponse.getBody().isCompleted());
    }

    @Test
    void testDeleteMethodNotInList() {
        init();
        ResponseEntity<?> todo = controller.delete("1");
        assertEquals(todo, ResponseEntity.badRequest().build());
    }

    @Test
    void testDeleteMethodInList() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        controller.create(todo);
		ResponseEntity<Todo> actualTodoReadResponse = controller.read("1");

        assertEquals(actualTodoReadResponse.getBody().getId(), "1");
        assertEquals(actualTodoReadResponse.getBody().getTitle(), "test todo");
        assertEquals(actualTodoReadResponse.getBody().getDescription(), "test description");
        assertFalse(actualTodoReadResponse.getBody().isCompleted());

		ResponseEntity<?> actualDeleteResponse = controller.delete("1");
        assertEquals(actualDeleteResponse, ResponseEntity.ok().build());
    }

    @Test
    void testListMethodEmptyList() {
        /// if condition in original code is dead code
        init();
		ResponseEntity<List<Todo>> todos = controller.list();
		assertEquals(0,todos.getBody().size());
    }

    @Test
    void testListMethodNotEmptyList() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        controller.create(todo);
		ResponseEntity<List<Todo>> todos = controller.list();
		assertEquals(1,todos.getBody().size());
		Todo actualTodo = todos.getBody().get(0);
		assertEquals("1",actualTodo.getId());
		assertEquals("test todo",actualTodo.getTitle());
		assertEquals("test description",actualTodo.getDescription());
		assertFalse(actualTodo.isCompleted());
    }

    @Test
    void testListCompletedMethodNoCompleted() {
        ///if condition in original code is dead code
        init();
		ResponseEntity<List<Todo>> todos = controller.listCompleted();
		assertEquals(0,todos.getBody().size());
    }

    @Test
    void testListCompletedMethodCompleted() {
        init();
        TodoCreateRequest todo = new TodoCreateRequest("test todo", "test description");
        controller.create(todo);
        controller.update("1", true);
		ResponseEntity<List<Todo>> completedTodos = controller.listCompleted();

		assertEquals(1,completedTodos.getBody().size());
		Todo actualTodo = completedTodos.getBody().get(0);
		assertEquals("1",actualTodo.getId());
		assertEquals("test todo",actualTodo.getTitle());
		assertEquals("test description",actualTodo.getDescription());
		assertTrue(actualTodo.isCompleted());
    }

}
