package com.example.pratica4.repository;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para AlunoRepository.
 *
 * @DataJpaTest - Configura contexto JPA mínimo para testes de repository
 * - Usa banco H2 em memória
 * - Faz rollback automático após cada teste
 * - Não carrega toda a aplicação (mais rápido)
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do AlunoRepository")
class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno alunoTeste;

    @BeforeEach
    void setUp() {
        // Limpa o banco antes de cada teste
        alunoRepository.deleteAll();

        // Cria aluno de teste
        alunoTeste = Aluno.builder()
                .nome("João Silva")
                .cpf(new CPF("12345678901"))
                .email(new Email("joao@teste.com"))
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.now())
                .beneficiosPremiumAtivos(false)
                .build();
    }

    @Test
    @DisplayName("Deve salvar aluno com sucesso")
    void deveSalvarAlunoComSucesso() {
        Aluno alunoSalvo = alunoRepository.save(alunoTeste);

        assertNotNull(alunoSalvo.getId());
        assertEquals("João Silva", alunoSalvo.getNome());
        assertEquals("12345678901", alunoSalvo.getCpf().getNumero());
        assertEquals("joao@teste.com", alunoSalvo.getEmail().getEndereco());
    }

    @Test
    @DisplayName("Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() {
        Aluno alunoSalvo = alunoRepository.save(alunoTeste);

        Optional<Aluno> resultado = alunoRepository.findById(alunoSalvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals(alunoSalvo.getId(), resultado.get().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar ID inexistente")
    void deveRetornarVazioAoBuscarIdInexistente() {
        Optional<Aluno> resultado = alunoRepository.findById(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve buscar aluno por CPF")
    void deveBuscarAlunoPorCpf() {
        alunoRepository.save(alunoTeste);

        Optional<Aluno> resultado = alunoRepository.findByCpf(new CPF("12345678901"));

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve buscar aluno por Email")
    void deveBuscarAlunoPorEmail() {
        alunoRepository.save(alunoTeste);

        Optional<Aluno> resultado = alunoRepository.findByEmail(new Email("joao@teste.com"));

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve verificar se CPF existe")
    void deveVerificarSeCpfExiste() {
        alunoRepository.save(alunoTeste);

        boolean existe = alunoRepository.existsByCpf(new CPF("12345678901"));
        boolean naoExiste = alunoRepository.existsByCpf(new CPF("99999999999"));

        assertTrue(existe);
        assertFalse(naoExiste);
    }

    @Test
    @DisplayName("Deve verificar se Email existe")
    void deveVerificarSeEmailExiste() {
        alunoRepository.save(alunoTeste);

        boolean existe = alunoRepository.existsByEmail(new Email("joao@teste.com"));
        boolean naoExiste = alunoRepository.existsByEmail(new Email("outro@teste.com"));

        assertTrue(existe);
        assertFalse(naoExiste);
    }

    @Test
    @DisplayName("Deve listar alunos por plano")
    void deveListarAlunosPorPlano() {
        // Salva alunos com planos diferentes
        alunoRepository.save(alunoTeste); // BASICO

        Aluno alunoPremium = Aluno.builder()
                .nome("Maria Santos")
                .cpf(new CPF("98765432109"))
                .email(new Email("maria@teste.com"))
                .plano(Plano.PREMIUM)
                .dataCadastro(LocalDate.now())
                .beneficiosPremiumAtivos(true)
                .build();
        alunoRepository.save(alunoPremium);

        List<Aluno> alunosBasico = alunoRepository.findByPlano(Plano.BASICO);
        List<Aluno> alunosPremium = alunoRepository.findByPlano(Plano.PREMIUM);

        assertEquals(1, alunosBasico.size());
        assertEquals(1, alunosPremium.size());
        assertEquals("João Silva", alunosBasico.get(0).getNome());
        assertEquals("Maria Santos", alunosPremium.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar alunos com benefícios premium ativos")
    void deveListarAlunosComBeneficiosPremiumAtivos() {
        alunoRepository.save(alunoTeste); // sem benefícios

        Aluno alunoComBeneficios = Aluno.builder()
                .nome("Carlos Premium")
                .cpf(new CPF("11111111111"))
                .email(new Email("carlos@teste.com"))
                .plano(Plano.PREMIUM)
                .dataCadastro(LocalDate.now())
                .beneficiosPremiumAtivos(true)
                .build();
        alunoRepository.save(alunoComBeneficios);

        List<Aluno> alunosComBeneficios = alunoRepository.findByBeneficiosPremiumAtivosTrue();

        assertEquals(1, alunosComBeneficios.size());
        assertEquals("Carlos Premium", alunosComBeneficios.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosAlunos() {
        alunoRepository.save(alunoTeste);

        Aluno outroAluno = Aluno.builder()
                .nome("Pedro Oliveira")
                .cpf(new CPF("22222222222"))
                .email(new Email("pedro@teste.com"))
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.now())
                .build();
        alunoRepository.save(outroAluno);

        List<Aluno> todos = alunoRepository.findAll();

        assertEquals(2, todos.size());
    }

    @Test
    @DisplayName("Deve deletar aluno por ID")
    void deveDeletarAlunoPorId() {
        Aluno alunoSalvo = alunoRepository.save(alunoTeste);

        alunoRepository.deleteById(alunoSalvo.getId());

        Optional<Aluno> resultado = alunoRepository.findById(alunoSalvo.getId());
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve contar alunos")
    void deveContarAlunos() {
        assertEquals(0, alunoRepository.count());

        alunoRepository.save(alunoTeste);

        assertEquals(1, alunoRepository.count());
    }

    @Test
    @DisplayName("Deve salvar aluno com cursos (cascade)")
    void deveSalvarAlunoComCursos() {
        Curso curso1 = Curso.builder()
                .nome("Java Básico")
                .media(8.5)
                .concluido(true)
                .build();

        Curso curso2 = Curso.builder()
                .nome("Spring Boot")
                .media(9.0)
                .concluido(true)
                .build();

        alunoTeste.adicionarCurso(curso1);
        alunoTeste.adicionarCurso(curso2);

        Aluno alunoSalvo = alunoRepository.save(alunoTeste);

        // Recarrega do banco
        Aluno alunoRecarregado = alunoRepository.findById(alunoSalvo.getId()).get();

        assertEquals(2, alunoRecarregado.getCursos().size());
        assertNotNull(alunoRecarregado.getCursos().get(0).getId());
        assertNotNull(alunoRecarregado.getCursos().get(1).getId());
    }

    @Test
    @DisplayName("Deve atualizar aluno existente")
    void deveAtualizarAlunoExistente() {
        Aluno alunoSalvo = alunoRepository.save(alunoTeste);

        alunoSalvo.setNome("João Silva Junior");
        alunoSalvo.setPlano(Plano.PREMIUM);

        Aluno alunoAtualizado = alunoRepository.save(alunoSalvo);

        assertEquals("João Silva Junior", alunoAtualizado.getNome());
        assertEquals(Plano.PREMIUM, alunoAtualizado.getPlano());
    }
}
