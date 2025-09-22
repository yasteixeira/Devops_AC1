package com.example.pratica4.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    private Aluno aluno;
    private static final double MEDIA_APROVACAO = 7.0;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("João Silva");
    }

    @Test
    void deve_iniciar_com_plano_basico() {
        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    void deve_contar_cursos_aprovados_corretamente() {
        adicionarCursosAprovados(5, 7.5);
        adicionarCursosReprovados(2, 6.0);

        assertEquals(5, aluno.getCursosConcluidosComMediaSete());
    }

    @Test
    void nao_deve_fazer_upgrade_com_11_cursos_aprovados() {
        adicionarCursosAprovados(11, MEDIA_APROVACAO);

        aluno.atualizarPlano();

        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    void deve_fazer_upgrade_com_12_cursos_aprovados() {
        adicionarCursosAprovados(12, MEDIA_APROVACAO);

        aluno.atualizarPlano();

        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    void deve_fazer_upgrade_com_mais_de_12_cursos_aprovados() {
        adicionarCursosAprovados(15, 8.0);

        aluno.atualizarPlano();

        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    void nao_deve_fazer_upgrade_com_12_cursos_mas_apenas_10_aprovados() {
        adicionarCursosAprovados(10, 7.5);
        adicionarCursosReprovados(2, 6.0);

        aluno.atualizarPlano();

        assertEquals(12, aluno.getCursos().size()); // Total de cursos
        assertEquals(10, aluno.getCursosConcluidosComMediaSete()); // Apenas 10 aprovados
        assertEquals(Plano.BASICO, aluno.getPlano()); // Deve permanecer básico
        assertFalse(aluno.isBeneficiosPremiumAtivos()); // Sem benefícios premium
    }

    @Test
    void deve_calcular_progresso_corretamente() {
        adicionarCursosAprovados(8, 7.2);
        adicionarCursosReprovados(3, 5.5);

        assertEquals(11, aluno.getCursos().size());
        assertEquals(8, aluno.getCursosConcluidosComMediaSete());

        String progresso = aluno.getCursosConcluidosComMediaSete() + "/12 cursos válidos";
        assertEquals("8/12 cursos válidos", progresso);
    }

    @Test
    void deve_manter_basico_quando_exactamente_no_limite_inferior() {
        adicionarCursosAprovados(11, MEDIA_APROVACAO);
        // 1 curso reprovado ligeiramente abaixo do limite
        adicionarCursosReprovados(1, 6.9);

        aluno.atualizarPlano();

        assertEquals(12, aluno.getCursos().size());
        assertEquals(11, aluno.getCursosConcluidosComMediaSete());
        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    // Helper methods to reduce duplication and make intent explicit
    private void adicionarCursosAprovados(int quantidade, double media) {
        for (int i = 0; i < quantidade; i++) {
            aluno.adicionarCurso(criarCursoConcluido(media, "Aprovado " + (i + 1)));
        }
    }

    private void adicionarCursosReprovados(int quantidade, double media) {
        for (int i = 0; i < quantidade; i++) {
            aluno.adicionarCurso(criarCursoConcluido(media, "Reprovado " + (i + 1)));
        }
    }

    private Curso criarCursoConcluido(double media, String nome) {
        Curso curso = new Curso(nome);
        curso.concluir(media);
        return curso;
    }
}