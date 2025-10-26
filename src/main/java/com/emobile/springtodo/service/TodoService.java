package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TodoDTO;
import com.emobile.springtodo.dto.TodoRequestDTO;
import com.emobile.springtodo.entity.Todo;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
        repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Cacheable(value = "todosCompleted")
    public List<TodoDTO> findCompleted() {
        return mapper.toDTOs(repository.findByCompleted(true));
    }

    @Cacheable(value = "todosPending")
    public List<TodoDTO> findPending() {
        return mapper.toDTOs(repository.findByCompleted(false));
    }

    @Cacheable(value = "todosPaginated", key = "#limit + '-' + #offset")
    public List<TodoDTO> findPagination(int limit, int offset) {
        return mapper.toDTOs(repository.findAll(PageRequest.of(offset/limit, limit)).getContent());
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
        repository.save(todo);
        return mapper.toDTO(todo);
    }

    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "todo", key = "#id"),
            @CacheEvict(value = "todosCompleted", allEntries = true),
            @CacheEvict(value = "todosPending", allEntries = true)
    })
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        repository.deleteById(id);
    }
}
