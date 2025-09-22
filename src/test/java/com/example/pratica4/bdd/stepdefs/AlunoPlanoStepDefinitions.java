package com.example.pratica4.bdd.stepdefs;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.*;

public class AlunoPlanoStepDefinitions {

    private Aluno aluno;

    @Dado("que existe um aluno chamado {string}")
    public void que_existe_um_aluno_chamado(String nome) {
        aluno = new Aluno(nome);
    }

    @Dado("que o aluno tem {int} cursos concluídos com média {double}")
    public void que_o_aluno_tem_cursos_concluidos_com_media(int quantidade, double media) {
        for (int i = 0; i < quantidade; i++) {
            Curso curso = new Curso("Curso " + (i + 1) + " (média " + media + ")");
            curso.concluir(media);
            aluno.adicionarCurso(curso);

            // Debug: verificar se o curso foi criado corretamente
            System.out.println("DEBUG: Criado curso " + curso.getNome() +
                    " - Média: " + curso.getMedia() +
                    " - Concluído: " + curso.isConcluido() +
                    " - Aprovado: " + curso.isAprovado());
        }
    }

    @Quando("o plano for atualizado")
    public void o_plano_for_atualizado() {
        aluno.atualizarPlano();
    }

    @Então("o aluno deve ter plano {string}")
    public void o_aluno_deve_ter_plano(String planoEsperado) {
        Plano plano = Plano.valueOf(planoEsperado);
        assertEquals(plano, aluno.getPlano());
    }

    @Então("os benefícios premium devem estar {string}")
    public void os_beneficios_premium_devem_estar(String status) {
        boolean esperado = "ativados".equals(status);
        assertEquals(esperado, aluno.isBeneficiosPremiumAtivos());
    }

    @Então("o aluno deve ter {int} cursos no total")
    public void o_aluno_deve_ter_cursos_no_total(int totalEsperado) {
        assertEquals(totalEsperado, aluno.getCursos().size());
    }

    @Então("o aluno deve ter {int} cursos aprovados")
    public void o_aluno_deve_ter_cursos_aprovados(int aprovadosEsperados) {
        // Debug: vamos verificar o que está acontecendo
        long aprovadosReais = aluno.getCursosConcluidosComMediaSete();
        int totalCursos = aluno.getCursos().size();

        System.out.println("DEBUG: Total de cursos: " + totalCursos);
        System.out.println("DEBUG: Cursos aprovados: " + aprovadosReais);
        System.out.println("DEBUG: Esperado: " + aprovadosEsperados);

        assertEquals(aprovadosEsperados, (int) aprovadosReais,
                "Esperava " + aprovadosEsperados + " cursos aprovados, mas encontrou " + aprovadosReais +
                        " (total de cursos: " + totalCursos + ")");
    }

    @Então("o progresso deve mostrar {string}")
    public void o_progresso_deve_mostrar(String progressoEsperado) {
        // Corrigido: usa o método correto que conta apenas cursos aprovados
        String progresso = aluno.getCursosConcluidosComMediaSete() + "/12 cursos válidos";
        assertEquals(progressoEsperado, progresso);
    }
}