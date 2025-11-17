package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ProgressoDTO")
class ProgressoDTOTest {

    private Aluno aluno;
    private ProgressoDTO progressoDTO;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("João Silva");

        progressoDTO = ProgressoDTO.builder()
                .nomeAluno("João Silva")
                .planoAtual(Plano.BASICO)
                .cursosAprovados(6)
                .cursosNecessarios(12)
                .cursosRestantes(6)
                .percentualProgresso(50.0)
                .elegívelParaUpgrade(false)
                .mensagem("Continue assim!")
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        ProgressoDTO dto = new ProgressoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        ProgressoDTO dto = new ProgressoDTO(
                "Maria",
                Plano.PREMIUM,
                12,
                12,
                0,
                100.0,
                true,
                "Parabéns!"
        );

        assertEquals("Maria", dto.getNomeAluno());
        assertEquals(Plano.PREMIUM, dto.getPlanoAtual());
        assertEquals(12, dto.getCursosAprovados());
        assertTrue(dto.isElegívelParaUpgrade());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        ProgressoDTO dto = ProgressoDTO.builder()
                .nomeAluno("Pedro")
                .planoAtual(Plano.BASICO)
                .cursosAprovados(8)
                .cursosNecessarios(12)
                .cursosRestantes(4)
                .percentualProgresso(66.67)
                .elegívelParaUpgrade(false)
                .mensagem("Quase lá!")
                .build();

        assertEquals("Pedro", dto.getNomeAluno());
        assertEquals(8, dto.getCursosAprovados());
        assertEquals(4, dto.getCursosRestantes());
        assertEquals(66.67, dto.getPercentualProgresso());
    }

    @Test
    @DisplayName("FromAluno deve criar ProgressoDTO com 0 cursos aprovados")
    void from_aluno_deve_criar_com_zero_cursos() {
        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNomeAluno());
        assertEquals(Plano.BASICO, resultado.getPlanoAtual());
        assertEquals(0, resultado.getCursosAprovados());
        assertEquals(12, resultado.getCursosNecessarios());
        assertEquals(12, resultado.getCursosRestantes());
        assertEquals(0.0, resultado.getPercentualProgresso());
        assertFalse(resultado.isElegívelParaUpgrade());
        assertNotNull(resultado.getMensagem());
        assertTrue(resultado.getMensagem().contains("12"));
    }

    @Test
    @DisplayName("FromAluno deve calcular progresso para 6 cursos aprovados")
    void from_aluno_deve_calcular_progresso_6_cursos() {
        // Adiciona 6 cursos aprovados
        for (int i = 0; i < 6; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(7.5);
            aluno.adicionarCurso(curso);
        }

        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertEquals(6, resultado.getCursosAprovados());
        assertEquals(6, resultado.getCursosRestantes());
        assertEquals(50.0, resultado.getPercentualProgresso());
        assertFalse(resultado.isElegívelParaUpgrade());
        assertTrue(resultado.getMensagem().contains("metade"));
    }

    @Test
    @DisplayName("FromAluno deve calcular progresso para 10 cursos aprovados")
    void from_aluno_deve_calcular_progresso_10_cursos() {
        // Adiciona 10 cursos aprovados
        for (int i = 0; i < 10; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(8.0);
            aluno.adicionarCurso(curso);
        }

        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertEquals(10, resultado.getCursosAprovados());
        assertEquals(2, resultado.getCursosRestantes());
        assertEquals(83.33, resultado.getPercentualProgresso());
        assertFalse(resultado.isElegívelParaUpgrade());
        assertTrue(resultado.getMensagem().contains("quase"));
    }

    @Test
    @DisplayName("FromAluno deve marcar elegível com 12 cursos aprovados")
    void from_aluno_deve_marcar_elegivel_com_12_cursos() {
        // Adiciona 12 cursos aprovados
        for (int i = 0; i < 12; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(7.0);
            aluno.adicionarCurso(curso);
        }

        aluno.atualizarPlano();

        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertEquals(12, resultado.getCursosAprovados());
        assertEquals(0, resultado.getCursosRestantes());
        assertEquals(100.0, resultado.getPercentualProgresso());
        assertTrue(resultado.isElegívelParaUpgrade());
        assertTrue(resultado.getMensagem().contains("Parabéns"));
    }

    @Test
    @DisplayName("FromAluno deve lidar com mais de 12 cursos aprovados")
    void from_aluno_deve_lidar_com_mais_de_12_cursos() {
        // Adiciona 15 cursos aprovados
        for (int i = 0; i < 15; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(9.0);
            aluno.adicionarCurso(curso);
        }

        aluno.atualizarPlano();

        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertEquals(15, resultado.getCursosAprovados());
        assertEquals(0, resultado.getCursosRestantes());
        assertEquals(125.0, resultado.getPercentualProgresso());
        assertTrue(resultado.isElegívelParaUpgrade());
        assertEquals(Plano.PREMIUM, resultado.getPlanoAtual());
    }

    @Test
    @DisplayName("FromAluno deve ignorar cursos não aprovados")
    void from_aluno_deve_ignorar_cursos_nao_aprovados() {
        // Adiciona 5 cursos aprovados e 3 reprovados
        for (int i = 0; i < 5; i++) {
            Curso curso = new Curso("Aprovado " + i);
            curso.concluir(7.5);
            aluno.adicionarCurso(curso);
        }

        for (int i = 0; i < 3; i++) {
            Curso curso = new Curso("Reprovado " + i);
            curso.concluir(6.0);
            aluno.adicionarCurso(curso);
        }

        ProgressoDTO resultado = ProgressoDTO.fromAluno(aluno);

        assertEquals(5, resultado.getCursosAprovados());
        assertEquals(7, resultado.getCursosRestantes());
        assertEquals(41.67, resultado.getPercentualProgresso());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        progressoDTO.setNomeAluno("Novo Nome");
        progressoDTO.setPlanoAtual(Plano.PREMIUM);
        progressoDTO.setCursosAprovados(15);
        progressoDTO.setCursosNecessarios(12);
        progressoDTO.setCursosRestantes(0);
        progressoDTO.setPercentualProgresso(125.0);
        progressoDTO.setElegívelParaUpgrade(true);
        progressoDTO.setMensagem("Nova mensagem");

        assertEquals("Novo Nome", progressoDTO.getNomeAluno());
        assertEquals(Plano.PREMIUM, progressoDTO.getPlanoAtual());
        assertEquals(15, progressoDTO.getCursosAprovados());
        assertEquals(12, progressoDTO.getCursosNecessarios());
        assertEquals(0, progressoDTO.getCursosRestantes());
        assertEquals(125.0, progressoDTO.getPercentualProgresso());
        assertTrue(progressoDTO.isElegívelParaUpgrade());
        assertEquals("Nova mensagem", progressoDTO.getMensagem());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        ProgressoDTO dto1 = ProgressoDTO.builder()
                .nomeAluno("João")
                .planoAtual(Plano.BASICO)
                .cursosAprovados(6)
                .cursosNecessarios(12)
                .cursosRestantes(6)
                .percentualProgresso(50.0)
                .elegívelParaUpgrade(false)
                .mensagem("Teste")
                .build();

        ProgressoDTO dto2 = ProgressoDTO.builder()
                .nomeAluno("João")
                .planoAtual(Plano.BASICO)
                .cursosAprovados(6)
                .cursosNecessarios(12)
                .cursosRestantes(6)
                .percentualProgresso(50.0)
                .elegívelParaUpgrade(false)
                .mensagem("Teste")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = progressoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("João Silva") || toString.contains("nomeAluno"));
    }
}
