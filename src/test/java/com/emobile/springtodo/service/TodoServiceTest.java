package com.emobile.springtodo.service;

import com.emobile.springtodo.DTO.TodoDTO;
import com.emobile.springtodo.DTO.TodoRequestDTO;
import com.emobile.springtodo.entity.Todo;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    private TodoMapper mapper;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    void setUp() {
        mapper = new TodoMapper();
        service = new TodoService(repository, mapper);
    }

    @Test
    @DisplayName("findAll возвращает DTO список")
    void findAll_returnsDtos() {
        given(repository.findAll()).willReturn(List.of(new Todo(1L, "A", false)));

        List<TodoDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("A");
    }

    @Test
    @DisplayName("findById бросает TodoNotFoundException если записи нет")
    void findById_notFound_throws() {
        given(repository.findById(42L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DisplayName("create сохраняет и возвращает DTO")
    void create_savesAndReturnsDto() {
        TodoRequestDTO req = new TodoRequestDTO("New", false);
        Todo saved = new Todo(10L, "New", false);
        given(repository.save(any(Todo.class))).willReturn(saved);

        TodoDTO dto = service.create(req);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getTitle()).isEqualTo("New");
    }

    @Test
    @DisplayName("update обновляет существующую запись")
    void update_updatesExisting() {
        Todo existing = new Todo(5L, "Old", false);
        given(repository.findById(5L)).willReturn(Optional.of(existing));
        given(repository.update(eq(5L), any(Todo.class))).willReturn(1);

        TodoRequestDTO req = new TodoRequestDTO("Up", true);
        TodoDTO updated = service.update(5L, req);

        assertThat(updated.getTitle()).isEqualTo("Up");
        assertThat(updated.isCompleted()).isTrue();
        verify(repository).update(eq(5L), any(Todo.class));
    }

    @Test
    @DisplayName("delete бросает исключение если запись не найдена")
    void delete_notFound_throws() {
        given(repository.delete(77L)).willReturn(0);

        assertThatThrownBy(() -> service.delete(77L))
                .isInstanceOf(TodoNotFoundException.class);
    }
}




