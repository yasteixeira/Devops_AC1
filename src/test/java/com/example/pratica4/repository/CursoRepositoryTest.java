package com.example.pratica4.repository;

import com.example.pratica4.model.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para CursoRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do CursoRepository")
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void setUp() {
        cursoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar curso com sucesso")
    void deveSalvarCursoComSucesso() {
        Curso curso = Curso.builder()
                .nome("Java Avançado")
                .media(8.5)
                .concluido(true)
                .build();

        Curso cursoSalvo = cursoRepository.save(curso);

        assertNotNull(cursoSalvo.getId());
        assertEquals("Java Avançado", cursoSalvo.getNome());
        assertEquals(8.5, cursoSalvo.getMedia());
        assertTrue(cursoSalvo.isConcluido());
    }

    @Test
    @DisplayName("Deve buscar curso por ID")
    void deveBuscarCursoPorId() {
        Curso curso = cursoRepository.save(new Curso("Teste"));

        Optional<Curso> resultado = cursoRepository.findById(curso.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Teste", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve buscar cursos por nome (ignorando case)")
    void deveBuscarCursosPorNome() {
        cursoRepository.save(new Curso("Java Básico"));
        cursoRepository.save(new Curso("Java Avançado"));
        cursoRepository.save(new Curso("Python Básico"));

        List<Curso> cursosJava = cursoRepository.findByNomeContainingIgnoreCase("java");
        List<Curso> cursosPython = cursoRepository.findByNomeContainingIgnoreCase("PYTHON");

        assertEquals(2, cursosJava.size());
        assertEquals(1, cursosPython.size());
    }

    @Test
    @DisplayName("Deve listar apenas cursos concluídos")
    void deveListarApenasCursosConcluidos() {
        Curso concluido1 = Curso.builder()
                .nome("Curso 1")
                .media(7.0)
                .concluido(true)
                .build();

        Curso concluido2 = Curso.builder()
                .nome("Curso 2")
                .media(8.0)
                .concluido(true)
                .build();

        Curso naoConcluido = Curso.builder()
                .nome("Curso 3")
                .media(0.0)
                .concluido(false)
                .build();

        cursoRepository.save(concluido1);
        cursoRepository.save(concluido2);
        cursoRepository.save(naoConcluido);

        List<Curso> cursosConcluidos = cursoRepository.findByConcluidoTrue();

        assertEquals(2, cursosConcluidos.size());
    }

    @Test
    @DisplayName("Deve listar apenas cursos não concluídos")
    void deveListarApenasCursosNaoConcluidos() {
        cursoRepository.save(Curso.builder().nome("Concluído").concluido(true).media(7.0).build());
        cursoRepository.save(Curso.builder().nome("Em andamento 1").concluido(false).build());
        cursoRepository.save(Curso.builder().nome("Em andamento 2").concluido(false).build());

        List<Curso> cursosNaoConcluidos = cursoRepository.findByConcluidoFalse();

        assertEquals(2, cursosNaoConcluidos.size());
    }

    @Test
    @DisplayName("Deve listar cursos aprovados (concluído E média >= 7.0)")
    void deveListarCursosAprovados() {
        // Aprovados
        cursoRepository.save(Curso.builder().nome("Aprovado 1").concluido(true).media(7.0).build());
        cursoRepository.save(Curso.builder().nome("Aprovado 2").concluido(true).media(9.5).build());

        // Reprovados
        cursoRepository.save(Curso.builder().nome("Reprovado").concluido(true).media(5.0).build());

        // Não concluído
        cursoRepository.save(Curso.builder().nome("Em andamento").concluido(false).media(0.0).build());

        List<Curso> cursosAprovados = cursoRepository.findCursosAprovados();

        assertEquals(2, cursosAprovados.size());
        assertTrue(cursosAprovados.stream().allMatch(c -> c.getMedia() >= 7.0));
    }

    @Test
    @DisplayName("Deve listar cursos reprovados (concluído E média < 7.0)")
    void deveListarCursosReprovados() {
        cursoRepository.save(Curso.builder().nome("Aprovado").concluido(true).media(8.0).build());
        cursoRepository.save(Curso.builder().nome("Reprovado 1").concluido(true).media(5.0).build());
        cursoRepository.save(Curso.builder().nome("Reprovado 2").concluido(true).media(6.5).build());

        List<Curso> cursosReprovados = cursoRepository.findCursosReprovados();

        assertEquals(2, cursosReprovados.size());
        assertTrue(cursosReprovados.stream().allMatch(c -> c.getMedia() < 7.0));
    }

    @Test
    @DisplayName("Deve contar cursos aprovados")
    void deveContarCursosAprovados() {
        cursoRepository.save(Curso.builder().nome("Aprovado 1").concluido(true).media(7.0).build());
        cursoRepository.save(Curso.builder().nome("Aprovado 2").concluido(true).media(10.0).build());
        cursoRepository.save(Curso.builder().nome("Reprovado").concluido(true).media(4.0).build());

        long count = cursoRepository.countCursosAprovados();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Deve deletar curso")
    void deveDeletarCurso() {
        Curso curso = cursoRepository.save(new Curso("Para deletar"));

        cursoRepository.deleteById(curso.getId());

        assertFalse(cursoRepository.findById(curso.getId()).isPresent());
    }

    @Test
    @DisplayName("Deve atualizar curso")
    void deveAtualizarCurso() {
        Curso curso = cursoRepository.save(new Curso("Original"));

        curso.setNome("Atualizado");
        curso.concluir(9.0);

        Curso cursoAtualizado = cursoRepository.save(curso);

        assertEquals("Atualizado", cursoAtualizado.getNome());
        assertEquals(9.0, cursoAtualizado.getMedia());
        assertTrue(cursoAtualizado.isConcluido());
    }

    @Test
    @DisplayName("Deve verificar se curso está aprovado através do método isAprovado()")
    void deveVerificarAprovacaoAtravesDoMetodo() {
        Curso aprovado = Curso.builder().nome("Aprovado").concluido(true).media(7.5).build();
        Curso reprovado = Curso.builder().nome("Reprovado").concluido(true).media(6.0).build();
        Curso emAndamento = Curso.builder().nome("Em andamento").concluido(false).media(0.0).build();

        assertTrue(aprovado.isAprovado());
        assertFalse(reprovado.isAprovado());
        assertFalse(emAndamento.isAprovado());
    }

    @Test
    @DisplayName("Deve listar todos os cursos")
    void deveListarTodosCursos() {
        cursoRepository.save(new Curso("Curso 1"));
        cursoRepository.save(new Curso("Curso 2"));
        cursoRepository.save(new Curso("Curso 3"));

        List<Curso> todos = cursoRepository.findAll();

        assertEquals(3, todos.size());
    }

    @Test
    @DisplayName("Deve contar total de cursos")
    void deveContarTotalCursos() {
        assertEquals(0, cursoRepository.count());

        cursoRepository.save(new Curso("Curso 1"));
        cursoRepository.save(new Curso("Curso 2"));

        assertEquals(2, cursoRepository.count());
    }
}
