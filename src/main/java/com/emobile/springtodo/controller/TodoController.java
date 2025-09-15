package com.emobile.springtodo.controller;

import com.emobile.springtodo.DTO.TodoDTO;
import com.emobile.springtodo.DTO.TodoRequestDTO;
import com.emobile.springtodo.config.TodoApi;
import com.emobile.springtodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController implements TodoApi {
    private final TodoService service;

    @Override
    public List<TodoDTO> findAll() {
        return service.findAll();
    }

    @Override
    public List<TodoDTO> findPagination(int limit, int offset) {
        return service.findPagination(limit, offset);
    }

    @Override
    public TodoDTO findById(Long id) {
        return service.findById(id);
    }

    @Override
    public TodoDTO create(TodoRequestDTO todo) {
        return service.create(todo);
    }

    @Override
    public TodoDTO update(Long id, TodoRequestDTO updatedTodo) {
        return service.update(id, updatedTodo);
    }

    @Override
    public List<TodoDTO> findCompleted() {
        return service.findCompleted();
    }

    @Override
    public List<TodoDTO> findPending() {
        return service.findPending();
    }

    @Override
    public void delete(Long id) {
         service.delete(id);
    }
}
