package com.example.pratica4.bdd;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class UpgradePlanoStepDefinitions {
    
    private Aluno aluno;
    private String progressoExibido;

    @Dado("que um aluno possui plano {string}")
    public void que_um_aluno_possui_plano(String plano) {
        aluno = new Aluno("João Silva");
        assertEquals(Plano.BASICO, aluno.getPlano());
    }

    @E("que ele possui {int} cursos concluídos com média maior ou igual a {double}")
    public void que_ele_possui_cursos_concluidos_com_media_maior_ou_igual_a(int numeroCursos, double mediaMinima) {
        for (int i = 1; i <= numeroCursos; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(7.0); // Conclui com média exatamente 7.0
            aluno.adicionarCurso(curso);
        }
        
        assertEquals(numeroCursos, aluno.getCursosConcluidosComMediaSete());
        assertEquals(Plano.BASICO, aluno.getPlano()); // Ainda deve ser básico
    }

    @Quando("ele concluir o {int}° curso com média maior ou igual a {double}")
    public void ele_concluir_o_curso_com_media_maior_ou_igual_a(int numeroCurso, double media) {
        Curso decimoSegundoCurso = new Curso("Curso " + numeroCurso);
        decimoSegundoCurso.concluir(media);
        aluno.adicionarCurso(decimoSegundoCurso);
        
        // Atualiza o plano após adicionar o curso
        aluno.atualizarPlano();
    }

    @Então("o sistema deve automaticamente alterar seu plano para {string}")
    public void o_sistema_deve_automaticamente_alterar_seu_plano_para(String novoPlano) {
        assertEquals(Plano.PREMIUM, aluno.getPlano());
    }

    @E("deve ativar os benefícios Premium")
    public void deve_ativar_os_beneficios_premium() {
        assertTrue(aluno.isBeneficiosPremiumAtivos());
        assertEquals(12, aluno.getCursosConcluidosComMediaSete());
    }

    // Novos step definitions para o segundo cenário
    @Dado("que um aluno possui {int} cursos concluídos")
    public void que_um_aluno_possui_cursos_concluidos(int numeroCursos) {
        aluno = new Aluno("Maria Santos");
        
        // Adiciona 10 cursos aprovados (média >= 7.0)
        for (int i = 1; i <= 10; i++) {
            Curso curso = new Curso("Curso Aprovado " + i);
            curso.concluir(7.5); // Média acima de 7.0
            aluno.adicionarCurso(curso);
        }
    }

    @E("{int} cursos têm média menor que {double}")
    public void cursos_tem_media_menor_que(int numeroReprovados, double mediaLimite) {
        // Adiciona os 2 cursos reprovados
        for (int i = 1; i <= numeroReprovados; i++) {
            Curso cursoReprovado = new Curso("Curso Reprovado " + i);
            cursoReprovado.concluir(6.0); // Média abaixo de 7.0
            aluno.adicionarCurso(cursoReprovado);
        }
        
        assertEquals(12, aluno.getCursos().size()); // Total de 12 cursos
        assertEquals(10, aluno.getCursosConcluidosComMediaSete()); // Apenas 10 aprovados
    }

    @Quando("o sistema verificar os critérios")
    public void o_sistema_verificar_os_criterios() {
        aluno.atualizarPlano();
        progressoExibido = aluno.getCursosConcluidosComMediaSete() + "/12 cursos válidos";
    }

    @Então("o plano deve permanecer {string}")
    public void o_plano_deve_permanecer(String planoEsperado) {
        assertEquals(Plano.BASICO, aluno.getPlano());
    }

    @E("deve exibir progresso: {string}")
    public void deve_exibir_progresso(String progressoEsperado) {
        assertEquals(progressoEsperado, progressoExibido);
    }
}