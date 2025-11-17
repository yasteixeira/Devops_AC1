package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Plano;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO (Data Transfer Object) para transferência de dados de Aluno.
 *
 * Por que usar DTO?
 * 1. Não expõe a entidade JPA diretamente (segurança)
 * 2. Controla exatamente quais dados são enviados na API
 * 3. Evita problemas com lazy loading e serialização do Jackson
 * 4. Permite formatar dados de forma diferente da entidade
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {

    private Long id;
    private String nome;
    private String cpf; // Aqui já vem formatado (123.456.789-01)
    private String email;
    private Plano plano;
    private LocalDate dataCadastro;
    private boolean beneficiosPremiumAtivos;
    private int quantidadeCursosAprovados;
    private List<CursoDTO> cursos;

    /**
     * Converte Entidade -> DTO
     * Método estático para facilitar conversão
     */
    public static AlunoDTO fromEntity(Aluno aluno) {
        if (aluno == null) {
            return null;
        }

        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .cpf(aluno.getCpf() != null ? aluno.getCpf().getFormatado() : null)
                .email(aluno.getEmail() != null ? aluno.getEmail().getEndereco() : null)
                .plano(aluno.getPlano())
                .dataCadastro(aluno.getDataCadastro())
                .beneficiosPremiumAtivos(aluno.isBeneficiosPremiumAtivos())
                .quantidadeCursosAprovados((int) aluno.getCursosConcluidosComMediaSete())
                .cursos(aluno.getCursos().stream()
                        .map(CursoDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Converte DTO -> Entidade (para uso no service)
     */
    public Aluno toEntity() {
        Aluno aluno = Aluno.builder()
                .id(this.id)
                .nome(this.nome)
                .plano(this.plano)
                .dataCadastro(this.dataCadastro)
                .beneficiosPremiumAtivos(this.beneficiosPremiumAtivos)
                .build();

        // Conversão de cursos se necessário
        if (this.cursos != null) {
            this.cursos.forEach(cursoDTO -> aluno.adicionarCurso(cursoDTO.toEntity()));
        }

        return aluno;
    }
}
