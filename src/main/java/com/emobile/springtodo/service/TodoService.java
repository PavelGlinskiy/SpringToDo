package com.emobile.springtodo.service;

import com.emobile.springtodo.DTO.TodoDTO;
import com.emobile.springtodo.DTO.TodoRequestDTO;
import com.emobile.springtodo.entity.Todo;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;
    private final TodoMapper mapper;

    @Cacheable(value = "todos")
    public List<TodoDTO> findAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Cacheable(value = "todo", key = "#id")
    public TodoDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "todosCompleted", allEntries = true),
            @CacheEvict(value = "todosPending", allEntries = true)
    })
    public TodoDTO create(TodoRequestDTO dto) {
        Todo entity = mapper.toEntity(dto);
        Todo saved = repository.save(entity);
        return mapper.toDTO(saved);
    }


    @Cacheable(value = "todosCompleted")
    public List<TodoDTO> findCompleted() {
        return mapper.toDTOs(repository.findCompleted());
    }

    @Cacheable(value = "todosPending")
    public List<TodoDTO> findPending() {
        return mapper.toDTOs(repository.findPending());
    }

    @Cacheable(value = "todosPaginated", key = "#limit + '-' + #offset")
    public List<TodoDTO> findPagination(int limit, int offset) {
        return mapper.toDTOs(repository.findPaginated(limit, offset));
    }

    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "todo", key = "#id"),
            @CacheEvict(value = "todosCompleted", allEntries = true),
            @CacheEvict(value = "todosPending", allEntries = true)
    })
    public TodoDTO update(Long id, TodoRequestDTO dto) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        mapper.updateEntityFromDTO(dto, todo);
        repository.update(id, todo);
        return mapper.toDTO(todo);
    }

    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "todo", key = "#id"),
            @CacheEvict(value = "todosCompleted", allEntries = true),
            @CacheEvict(value = "todosPending", allEntries = true)
    })
    public void delete(Long id) {
        int rows = repository.delete(id);
        if (rows == 0) {
            throw new TodoNotFoundException(id);
        }
    }
}
