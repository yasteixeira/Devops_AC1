package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Plano;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar progresso do aluno em direção ao plano Premium.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressoDTO {

    private String nomeAluno;
    private Plano planoAtual;
    private int cursosAprovados;
    private int cursosNecessarios = 12;
    private int cursosRestantes;
    private double percentualProgresso;
    private boolean elegívelParaUpgrade;
    private String mensagem;

    /**
     * Cria DTO de progresso a partir de um Aluno
     */
    public static ProgressoDTO fromAluno(Aluno aluno) {
        int aprovados = (int) aluno.getCursosConcluidosComMediaSete();
        int restantes = Math.max(0, 12 - aprovados);
        double percentual = (aprovados / 12.0) * 100;
        boolean elegivel = aprovados >= 12;

        String mensagem;
        if (elegivel) {
            mensagem = "Parabéns! Você atingiu o plano Premium!";
        } else if (aprovados >= 10) {
            mensagem = "Você está quase lá! Faltam apenas " + restantes + " cursos!";
        } else if (aprovados >= 6) {
            mensagem = "Continue assim! Você já completou metade do caminho!";
        } else {
            mensagem = "Continue estudando! Faltam " + restantes + " cursos para o Premium.";
        }

        return ProgressoDTO.builder()
                .nomeAluno(aluno.getNome())
                .planoAtual(aluno.getPlano())
                .cursosAprovados(aprovados)
                .cursosNecessarios(12)
                .cursosRestantes(restantes)
                .percentualProgresso(Math.round(percentual * 100.0) / 100.0) // 2 casas decimais
                .elegívelParaUpgrade(elegivel)
                .mensagem(mensagem)
                .build();
    }
}
