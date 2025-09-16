package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TodoDTO;
import com.emobile.springtodo.dto.TodoRequestDTO;
import com.emobile.springtodo.entity.Todo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {

    public TodoDTO toDTO(Todo todo) {
        if (todo == null) return null;
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted()
        );
    }

    public List<TodoDTO> toDTOs(List<Todo> todos) {
        if (todos == null) return List.of();
        return todos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Todo toEntity(TodoRequestDTO dto) {
        if (dto == null) return null;
        return new Todo(
                dto.getTitle(),
                dto.isCompleted()
        );
    }

    public void updateEntityFromDTO(TodoRequestDTO dto, Todo entity) {
        if (dto == null || entity == null) return;
        entity.setTitle(dto.getTitle());
        entity.setCompleted(dto.isCompleted());
    }
}
