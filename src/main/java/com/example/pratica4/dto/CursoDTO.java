package com.example.pratica4.dto;

import com.example.pratica4.model.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Curso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {

    private Long id;
    private String nome;
    private double media;
    private boolean concluido;
    private boolean aprovado; // Campo calculado (concluido && media >= 7.0)

    /**
     * Converte Entidade -> DTO
     */
    public static CursoDTO fromEntity(Curso curso) {
        if (curso == null) {
            return null;
        }

        return CursoDTO.builder()
                .id(curso.getId())
                .nome(curso.getNome())
                .media(curso.getMedia())
                .concluido(curso.isConcluido())
                .aprovado(curso.isAprovado()) // Usa a lógica da entidade
                .build();
    }

    /**
     * Converte DTO -> Entidade
     */
    public Curso toEntity() {
        return Curso.builder()
                .id(this.id)
                .nome(this.nome)
                .media(this.media)
                .concluido(this.concluido)
                .build();
    }
}
