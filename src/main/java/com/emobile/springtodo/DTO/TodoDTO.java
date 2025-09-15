package com.emobile.springtodo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    private Long id;

    @Schema(description = "Название задачи", example = "Купить молоко")
    private String title;

    @Schema(description = "Статус выполнения задачи", example = "false")
    private boolean completed;
}
