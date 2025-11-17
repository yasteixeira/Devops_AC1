package com.example.pratica4.dto;

import com.example.pratica4.model.Curso;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação/adição de curso a um aluno.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCursoDTO {

    @NotBlank(message = "Nome do curso é obrigatório")
    private String nome;

    /**
     * Converte para entidade Curso
     */
    public Curso toEntity() {
        return new Curso(this.nome);
    }
}
