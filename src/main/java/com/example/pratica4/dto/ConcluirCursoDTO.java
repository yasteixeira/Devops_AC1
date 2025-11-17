package com.example.pratica4.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para concluir um curso com média.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcluirCursoDTO {

    @NotNull(message = "Média é obrigatória")
    @DecimalMin(value = "0.0", message = "Média não pode ser menor que 0.0")
    @DecimalMax(value = "10.0", message = "Média não pode ser maior que 10.0")
    private Double media;
}
