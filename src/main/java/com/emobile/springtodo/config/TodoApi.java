package com.emobile.springtodo.config;

import com.emobile.springtodo.DTO.TodoDTO;
import com.emobile.springtodo.DTO.TodoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/todo")
@Tag(name = "Todo API", description = "API для управления задачами")
public interface TodoApi {

    @Operation(summary = "Получить все задачи")
    @GetMapping("/find/all")
    List<TodoDTO> findAll();

    @Operation(summary = "Получить задачи с пагинацией")
    @GetMapping("/find")
    List<TodoDTO> findPagination(
            @Parameter(description = "Количество элементов") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Смещение") @RequestParam(defaultValue = "0") int offset
    );

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    TodoDTO findById(@Parameter(description = "ID задачи") @PathVariable Long id);

    @Operation(summary = "Создать новую задачу")
    @PostMapping("/add")
    TodoDTO create(@Parameter(description = "Данные задачи") @Valid @RequestBody TodoRequestDTO todo);

    @Operation(summary = "Обновить существующую задачу")
    @PutMapping("/update/{id}")
    TodoDTO update(
            @Parameter(description = "ID задачи") @PathVariable Long id,
            @Parameter(description = "Обновленные данные задачи") @Valid  @RequestBody TodoRequestDTO updatedTodo
    );

    @Operation(summary = "Получить выполненные задачи")
    @GetMapping("/find/completed")
    List<TodoDTO> findCompleted();

    @Operation(summary = "Получить ожидающие задачи")
    @GetMapping("/find/pending")
    List<TodoDTO> findPending();

    @Operation(summary = "Удалить задачу по ID")
    @DeleteMapping("/delete/{id}")
    void delete(@Parameter(description = "ID задачи") @PathVariable Long id);
}
