package com.emobile.springtodo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания/обновления задачи")
public class TodoRequestDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Schema(description = "Название задачи", example = "Купить молоко")
    private String title;

    @Schema(description = "Статус выполнения задачи", example = "false")
    private boolean completed;
}
