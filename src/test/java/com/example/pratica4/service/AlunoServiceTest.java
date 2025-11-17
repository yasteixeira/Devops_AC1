package com.example.pratica4.service;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import com.example.pratica4.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AlunoService usando Mockito.
 *
 * @ExtendWith(MockitoExtension.class) - Habilita mocks do Mockito
 * @Mock - Cria mock (simulação) do repository
 * @InjectMocks - Injeta os mocks no service
 *
 * Por que usar mocks?
 * - Testa apenas a lógica do service (isolado)
 * - Não precisa de banco de dados (mais rápido)
 * - Controla o comportamento das dependências
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AlunoService")
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno alunoTeste;

    @BeforeEach
    void setUp() {
        alunoTeste = Aluno.builder()
                .id(1L)
                .nome("João Silva")
                .cpf(new CPF("12345678901"))
                .email(new Email("joao@teste.com"))
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.now())
                .beneficiosPremiumAtivos(false)
                .build();
    }

    @Test
    @DisplayName("Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() {
        // Configura comportamento do mock
        when(alunoRepository.existsByCpf(any(CPF.class))).thenReturn(false);
        when(alunoRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.criarAluno(alunoTeste);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(alunoRepository, times(1)).save(alunoTeste);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar aluno com CPF duplicado")
    void deveLancarExcecaoAoCriarAlunoComCpfDuplicado() {
        when(alunoRepository.existsByCpf(any(CPF.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.criarAluno(alunoTeste)
        );

        assertTrue(exception.getMessage().contains("CPF já cadastrado"));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar aluno com Email duplicado")
    void deveLancarExcecaoAoCriarAlunoComEmailDuplicado() {
        when(alunoRepository.existsByCpf(any(CPF.class))).thenReturn(false);
        when(alunoRepository.existsByEmail(any(Email.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.criarAluno(alunoTeste)
        );

        assertTrue(exception.getMessage().contains("Email já cadastrado"));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));

        Aluno resultado = alunoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar aluno inexistente")
    void deveLancarExcecaoAoBuscarAlunoInexistente() {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> alunoService.buscarPorId(999L)
        );

        assertTrue(exception.getMessage().contains("Aluno não encontrado"));
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosAlunos() {
        Aluno aluno2 = Aluno.builder()
                .id(2L)
                .nome("Maria Santos")
                .cpf(new CPF("98765432109"))
                .email(new Email("maria@teste.com"))
                .plano(Plano.PREMIUM)
                .build();

        when(alunoRepository.findAll()).thenReturn(Arrays.asList(alunoTeste, aluno2));

        List<Aluno> resultado = alunoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(alunoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar aluno")
    void deveAtualizarAluno() {
        Aluno alunoAtualizado = Aluno.builder()
                .nome("João Silva Junior")
                .build();

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.atualizarAluno(1L, alunoAtualizado);

        assertEquals("João Silva Junior", resultado.getNome());
        verify(alunoRepository, times(1)).save(alunoTeste);
    }

    @Test
    @DisplayName("Deve deletar aluno")
    void deveDeletarAluno() {
        when(alunoRepository.existsById(1L)).thenReturn(true);

        alunoService.deletarAluno(1L);

        verify(alunoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar aluno inexistente")
    void deveLancarExcecaoAoDeletarAlunoInexistente() {
        when(alunoRepository.existsById(999L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> alunoService.deletarAluno(999L)
        );

        assertTrue(exception.getMessage().contains("Aluno não encontrado"));
        verify(alunoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve adicionar curso ao aluno")
    void deveAdicionarCursoAoAluno() {
        Curso curso = new Curso("Java Básico");

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.adicionarCurso(1L, curso);

        assertEquals(1, resultado.getCursos().size());
        verify(alunoRepository, times(1)).save(alunoTeste);
    }

    @Test
    @DisplayName("Deve concluir curso com média (método com objetos)")
    void deveConcluirCursoComObjetos() {
        Aluno aluno = new Aluno("Teste");
        Curso curso = new Curso("Java");

        alunoService.concluirCurso(aluno, curso, 8.5);

        assertTrue(curso.isConcluido());
        assertEquals(8.5, curso.getMedia());
    }

    @Test
    @DisplayName("Deve concluir curso com média (método com IDs)")
    void deveConcluirCursoComIds() {
        Curso curso = Curso.builder()
                .id(1L)
                .nome("Java")
                .concluido(false)
                .media(0.0)
                .build();

        alunoTeste.adicionarCurso(curso);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.concluirCurso(1L, 1L, 7.5);

        assertTrue(curso.isConcluido());
        assertEquals(7.5, curso.getMedia());
        verify(alunoRepository, times(1)).save(alunoTeste);
    }

    @Test
    @DisplayName("Deve lançar exceção ao concluir curso inexistente")
    void deveLancarExcecaoAoConcluirCursoInexistente() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> alunoService.concluirCurso(1L, 999L, 7.0)
        );

        assertTrue(exception.getMessage().contains("Curso com ID 999 não encontrado"));
    }

    @Test
    @DisplayName("Deve verificar upgrade de plano (método com objeto)")
    void deveVerificarUpgradePlanoComObjeto() {
        Aluno aluno = new Aluno("Teste");

        // Adiciona 12 cursos aprovados
        for (int i = 0; i < 12; i++) {
            Curso curso = new Curso("Curso " + i);
            curso.concluir(7.0);
            aluno.adicionarCurso(curso);
        }

        alunoService.verificarUpgradePlano(aluno);

        assertEquals(Plano.PREMIUM, aluno.getPlano());
        assertTrue(aluno.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Deve verificar upgrade de plano (método com ID)")
    void deveVerificarUpgradePlanoComId() {
        // Adiciona 12 cursos aprovados
        for (int i = 0; i < 12; i++) {
            Curso curso = Curso.builder()
                    .id((long) i)
                    .nome("Curso " + i)
                    .concluido(true)
                    .media(7.5)
                    .build();
            alunoTeste.adicionarCurso(curso);
        }

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.verificarUpgradePlano(1L);

        assertEquals(Plano.PREMIUM, resultado.getPlano());
        assertTrue(resultado.isBeneficiosPremiumAtivos());
        verify(alunoRepository, times(1)).save(alunoTeste);
    }

    @Test
    @DisplayName("Não deve fazer upgrade com menos de 12 cursos aprovados")
    void naoDeveFazerUpgradeComMenosDe12Cursos() {
        // Adiciona apenas 11 cursos
        for (int i = 0; i < 11; i++) {
            Curso curso = Curso.builder()
                    .id((long) i)
                    .nome("Curso " + i)
                    .concluido(true)
                    .media(7.0)
                    .build();
            alunoTeste.adicionarCurso(curso);
        }

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.verificarUpgradePlano(1L);

        assertEquals(Plano.BASICO, resultado.getPlano());
        assertFalse(resultado.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Não deve fazer upgrade se cursos não atingirem média 7.0")
    void naoDeveFazerUpgradeSeMediaInsuficiente() {
        // Adiciona 12 cursos com média baixa
        for (int i = 0; i < 12; i++) {
            Curso curso = Curso.builder()
                    .id((long) i)
                    .nome("Curso " + i)
                    .concluido(true)
                    .media(6.5) // Abaixo de 7.0
                    .build();
            alunoTeste.adicionarCurso(curso);
        }

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoTeste));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoTeste);

        Aluno resultado = alunoService.verificarUpgradePlano(1L);

        assertEquals(Plano.BASICO, resultado.getPlano());
        assertFalse(resultado.isBeneficiosPremiumAtivos());
    }
}
