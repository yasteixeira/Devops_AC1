package com.example.pratica4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade JPA que representa um Curso.
 *
 * @Entity - Marca como entidade JPA
 * @Data - Lombok: gera getters, setters, equals, hashCode, toString
 * @Builder - Lombok: permite construção fluente de objetos
 */
@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do curso é obrigatório")
    @Column(nullable = false)
    private String nome;

    /**
     * Média do aluno no curso (0.0 a 10.0)
     */
    @DecimalMin(value = "0.0", message = "Média não pode ser menor que 0.0")
    @DecimalMax(value = "10.0", message = "Média não pode ser maior que 10.0")
    @Column(nullable = false)
    @Builder.Default
    private double media = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private boolean concluido = false;

    /**
     * Construtor para manter compatibilidade com testes BDD existentes
     */
    public Curso(String nome) {
        this.nome = nome;
        this.concluido = false;
        this.media = 0.0;
    }

    /**
     * Marca o curso como concluído e registra a média
     * @param media nota final do aluno (0.0 a 10.0)
     */
    public void concluir(double media) {
        this.media = media;
        this.concluido = true;
    }

    /**
     * Verifica se o aluno foi aprovado no curso
     * Critério: curso concluído E média >= 7.0
     * @return true se aprovado, false caso contrário
     */
    public boolean isAprovado() {
        return this.concluido && this.media >= 7.0;
    }
}
