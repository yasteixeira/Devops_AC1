package com.example.pratica4.model;

import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Aluno")
class AlunoTest {

    private Aluno aluno;
    private static final double MEDIA_APROVACAO = 7.0;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("João Silva");
    }

    @Test
    @DisplayName("Deve iniciar com plano básico")
    void deve_iniciar_com_plano_basico() {
        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Deve contar cursos aprovados corretamente")
    void deve_contar_cursos_aprovados_corretamente() {
        adicionarCursosAprovados(5, 7.5);
        adicionarCursosReprovados(2, 6.0);

        assertEquals(5, aluno.getCursosConcluidosComMediaSete());
    }

    @Test
    @DisplayName("Não deve fazer upgrade com 11 cursos aprovados")
    void nao_deve_fazer_upgrade_com_11_cursos_aprovados() {
        adicionarCursosAprovados(11, MEDIA_APROVACAO);

        aluno.atualizarPlano();

        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Deve fazer upgrade com 12 cursos aprovados")
    void deve_fazer_upgrade_com_12_cursos_aprovados() {
        adicionarCursosAprovados(12, MEDIA_APROVACAO);

        aluno.atualizarPlano();

        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Deve fazer upgrade com mais de 12 cursos aprovados")
    void deve_fazer_upgrade_com_mais_de_12_cursos_aprovados() {
        adicionarCursosAprovados(15, 8.0);

        aluno.atualizarPlano();

        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Não deve fazer upgrade com 12 cursos mas apenas 10 aprovados")
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
    @DisplayName("Deve calcular progresso corretamente")
    void deve_calcular_progresso_corretamente() {
        adicionarCursosAprovados(8, 7.2);
        adicionarCursosReprovados(3, 5.5);

        assertEquals(11, aluno.getCursos().size());
        assertEquals(8, aluno.getCursosConcluidosComMediaSete());

        String progresso = aluno.getCursosConcluidosComMediaSete() + "/12 cursos válidos";
        assertEquals("8/12 cursos válidos", progresso);
    }

    @Test
    @DisplayName("Deve manter básico quando exatamente no limite inferior")
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

    @Test
    @DisplayName("Deve retornar 0 cursos aprovados quando não tem cursos")
    void deve_retornar_zero_cursos_aprovados_quando_nao_tem_cursos() {
        Aluno alunoSemCursos = new Aluno("Teste");
        assertEquals(0, alunoSemCursos.getCursosConcluidosComMediaSete());
    }

    @Test
    @DisplayName("Deve atualizar para BASICO quando tem menos de 12 cursos")
    void deve_atualizar_para_basico_quando_tem_menos_de_12_cursos() {
        adicionarCursosAprovados(5, 7.5);
        aluno.atualizarPlano();

        assertEquals(Plano.BASICO, aluno.getPlano());
        assertFalse(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Builder deve criar aluno com todos os campos")
    void builder_deve_criar_aluno_com_todos_os_campos() {
        Aluno alunoBuilder = Aluno.builder()
                .id(10L)
                .nome("Maria Silva")
                .cpf(new CPF("98765432109"))
                .email(new Email("maria@teste.com"))
                .plano(Plano.PREMIUM)
                .dataCadastro(LocalDate.of(2023, 1, 1))
                .beneficiosPremiumAtivos(true)
                .build();

        assertEquals(10L, alunoBuilder.getId());
        assertEquals("Maria Silva", alunoBuilder.getNome());
        assertEquals("98765432109", alunoBuilder.getCpf().getNumero());
        assertEquals("maria@teste.com", alunoBuilder.getEmail().getEndereco());
        assertEquals(Plano.PREMIUM, alunoBuilder.getPlano());
        assertEquals(LocalDate.of(2023, 1, 1), alunoBuilder.getDataCadastro());
        assertTrue(alunoBuilder.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        Aluno alunoVazio = new Aluno();
        assertNotNull(alunoVazio);
        assertNotNull(alunoVazio.getCursos());
        assertTrue(alunoVazio.getCursos().isEmpty());
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        Aluno alunoCompleto = new Aluno(
                1L,
                "João Silva",
                new CPF("12345678901"),
                new Email("joao@teste.com"),
                Plano.BASICO,
                LocalDate.now(),
                false,
                java.util.List.of()
        );

        assertNotNull(alunoCompleto);
        assertEquals("João Silva", alunoCompleto.getNome());
    }

    @Test
    @DisplayName("Deve adicionar curso corretamente")
    void deve_adicionar_curso_corretamente() {
        Aluno alunoNovo = new Aluno("Teste");
        Curso curso = new Curso("Java Básico");

        alunoNovo.adicionarCurso(curso);

        assertEquals(1, alunoNovo.getCursos().size());
        assertTrue(alunoNovo.getCursos().contains(curso));
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        aluno.setId(100L);
        aluno.setNome("Novo Nome");
        aluno.setPlano(Plano.PREMIUM);
        aluno.setBeneficiosPremiumAtivos(true);
        aluno.setCpf(new CPF("11111111111"));
        aluno.setEmail(new Email("novo@email.com"));
        aluno.setDataCadastro(LocalDate.of(2024, 1, 1));

        assertEquals(100L, aluno.getId());
        assertEquals("Novo Nome", aluno.getNome());
        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
        assertEquals("11111111111", aluno.getCpf().getNumero());
        assertEquals("novo@email.com", aluno.getEmail().getEndereco());
        assertEquals(LocalDate.of(2024, 1, 1), aluno.getDataCadastro());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        Aluno aluno1 = Aluno.builder()
                .id(1L)
                .nome("João")
                .cpf(new CPF("12345678901"))
                .email(new Email("joao@teste.com"))
                .build();

        Aluno aluno2 = Aluno.builder()
                .id(1L)
                .nome("João")
                .cpf(new CPF("12345678901"))
                .email(new Email("joao@teste.com"))
                .build();

        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = aluno.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("João Silva"));
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