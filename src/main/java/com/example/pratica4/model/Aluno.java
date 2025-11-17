package com.example.pratica4.model;

import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA que representa um Aluno no sistema.
 *
 * @Entity - Marca como entidade JPA (será persistida no banco)
 * @Table - Define nome da tabela no banco de dados
 * @Data - Lombok: gera getters, setters, equals, hashCode, toString
 * @Builder - Lombok: permite construir objetos de forma fluente
 * @NoArgsConstructor - Lombok: construtor sem argumentos (obrigatório para JPA)
 * @AllArgsConstructor - Lombok: construtor com todos os argumentos
 */
@Entity
@Table(name = "alunos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    /**
     * ID auto-incrementado gerado pelo banco de dados
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    /**
     * CPF como Value Object (será armazenado na mesma tabela)
     * @Embedded - JPA incorpora os campos do CPF nesta tabela
     * @AttributeOverride - Renomeia a coluna para evitar conflito
     */
    @Embedded
    @AttributeOverride(name = "numero", column = @Column(name = "cpf", unique = true, nullable = false))
    private CPF cpf;

    /**
     * Email como Value Object
     */
    @Embedded
    @AttributeOverride(name = "endereco", column = @Column(name = "email", unique = true, nullable = false))
    private Email email;

    @NotNull(message = "Plano é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Plano plano = Plano.BASICO;

    @Column(name = "data_cadastro", nullable = false)
    @Builder.Default
    private LocalDate dataCadastro = LocalDate.now();

    @Column(name = "beneficios_premium_ativos")
    @Builder.Default
    private boolean beneficiosPremiumAtivos = false;

    /**
     * Relacionamento One-to-Many com Curso
     * cascade = CascadeType.ALL - Operações em Aluno afetam Cursos
     * orphanRemoval = true - Remove cursos órfãos automaticamente
     * fetch = FetchType.LAZY - Carrega cursos apenas quando necessário
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    @Builder.Default
    private List<Curso> cursos = new ArrayList<>();

    /**
     * Construtor para manter compatibilidade com testes BDD existentes
     */
    public Aluno(String nome) {
        this.nome = nome;
        this.plano = Plano.BASICO;
        this.cursos = new ArrayList<>();
        this.beneficiosPremiumAtivos = false;
        this.dataCadastro = LocalDate.now();
    }

    /**
     * Adiciona um curso à lista de cursos do aluno
     */
    public void adicionarCurso(Curso curso) {
        this.cursos.add(curso);
    }

    /**
     * Conta cursos concluídos com média >= 7.0 (aprovados)
     * @return quantidade de cursos aprovados
     */
    public long getCursosConcluidosComMediaSete() {
        return this.cursos.stream()
                .filter(c -> c.isConcluido() && c.isAprovado())
                .count();
    }

    /**
     * Atualiza o plano do aluno baseado na quantidade de cursos aprovados
     * Regra de negócio: 12+ cursos aprovados = upgrade para Premium
     */
    public void atualizarPlano() {
        long aprovados = getCursosConcluidosComMediaSete();
        if (aprovados >= 12) {
            this.plano = Plano.PREMIUM;
            this.beneficiosPremiumAtivos = true;
        } else {
            this.plano = Plano.BASICO;
            this.beneficiosPremiumAtivos = false;
        }
    }
}
