package com.example.pratica4.controller;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import com.example.pratica4.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para AlunoController.
 *
 * @WebMvcTest - Testa apenas a camada web (controllers)
 * @MockBean - Mock do service (não precisa de implementação real)
 * MockMvc - Simula requisições HTTP sem subir o servidor
 */
@WebMvcTest(AlunoController.class)
@ActiveProfiles("test")
@DisplayName("Testes do AlunoController")
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    @DisplayName("POST /api/alunos - Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nome", "João Silva");
        requestBody.put("cpf", "12345678901");
        requestBody.put("email", "joao@teste.com");

        when(alunoService.criarAluno(any(Aluno.class))).thenReturn(alunoTeste);

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-01"))
                .andExpect(jsonPath("$.email").value("joao@teste.com"))
                .andExpect(jsonPath("$.plano").value("BASICO"));
    }

    @Test
    @DisplayName("POST /api/alunos - Deve retornar 400 com dados inválidos")
    void deveRetornar400ComDadosInvalidos() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nome", "");
        requestBody.put("cpf", "123"); // CPF inválido
        requestBody.put("email", "email-invalido");

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/alunos - Deve listar todos os alunos")
    void deveListarTodosAlunos() throws Exception {
        Aluno aluno2 = Aluno.builder()
                .id(2L)
                .nome("Maria Santos")
                .cpf(new CPF("98765432109"))
                .email(new Email("maria@teste.com"))
                .plano(Plano.PREMIUM)
                .dataCadastro(LocalDate.now())
                .build();

        when(alunoService.listarTodos()).thenReturn(Arrays.asList(alunoTeste, aluno2));

        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));
    }

    @Test
    @DisplayName("GET /api/alunos/{id} - Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() throws Exception {
        when(alunoService.buscarPorId(1L)).thenReturn(alunoTeste);

        mockMvc.perform(get("/api/alunos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("GET /api/alunos/{id} - Deve retornar 404 quando aluno não existe")
    void deveRetornar404QuandoAlunoNaoExiste() throws Exception {
        when(alunoService.buscarPorId(999L))
                .thenThrow(new EntityNotFoundException("Aluno não encontrado com ID: 999"));

        mockMvc.perform(get("/api/alunos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aluno não encontrado com ID: 999"));
    }

    @Test
    @DisplayName("PUT /api/alunos/{id} - Deve atualizar aluno")
    void deveAtualizarAluno() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nome", "João Silva Junior");
        requestBody.put("cpf", "12345678901");
        requestBody.put("email", "joao@teste.com");

        alunoTeste.setNome("João Silva Junior");

        when(alunoService.atualizarAluno(eq(1L), any(Aluno.class))).thenReturn(alunoTeste);

        mockMvc.perform(put("/api/alunos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva Junior"));
    }

    @Test
    @DisplayName("DELETE /api/alunos/{id} - Deve deletar aluno")
    void deveDeletarAluno() throws Exception {
        mockMvc.perform(delete("/api/alunos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/alunos/{id}/cursos - Deve adicionar curso")
    void deveAdicionarCurso() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nome", "Java Básico");

        Curso curso = new Curso("Java Básico");
        alunoTeste.adicionarCurso(curso);

        when(alunoService.adicionarCurso(eq(1L), any(Curso.class))).thenReturn(alunoTeste);

        mockMvc.perform(post("/api/alunos/1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursos").isArray())
                .andExpect(jsonPath("$.cursos.length()").value(1));
    }

    @Test
    @DisplayName("PUT /api/alunos/{alunoId}/cursos/{cursoId}/concluir - Deve concluir curso")
    void deveConcluirCurso() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("media", 8.5);

        Curso curso = Curso.builder()
                .id(1L)
                .nome("Java")
                .concluido(true)
                .media(8.5)
                .build();
        alunoTeste.adicionarCurso(curso);

        when(alunoService.concluirCurso(1L, 1L, 8.5)).thenReturn(alunoTeste);

        mockMvc.perform(put("/api/alunos/1/cursos/1/concluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/alunos/{alunoId}/cursos/{cursoId}/concluir - Deve validar média")
    void deveValidarMedia() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("media", 11.0); // Média inválida (acima de 10.0)

        mockMvc.perform(put("/api/alunos/1/cursos/1/concluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/alunos/{id}/progresso - Deve consultar progresso")
    void deveConsultarProgresso() throws Exception {
        // Adiciona 6 cursos aprovados
        for (int i = 0; i < 6; i++) {
            Curso curso = Curso.builder()
                    .id((long) i)
                    .nome("Curso " + i)
                    .concluido(true)
                    .media(7.5)
                    .build();
            alunoTeste.adicionarCurso(curso);
        }

        when(alunoService.buscarPorId(1L)).thenReturn(alunoTeste);

        mockMvc.perform(get("/api/alunos/1/progresso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeAluno").value("João Silva"))
                .andExpect(jsonPath("$.planoAtual").value("BASICO"))
                .andExpect(jsonPath("$.cursosAprovados").value(6))
                .andExpect(jsonPath("$.cursosNecessarios").value(12))
                .andExpect(jsonPath("$.cursosRestantes").value(6))
                .andExpect(jsonPath("$.percentualProgresso").value(50.0))
                .andExpect(jsonPath("$.elegívelParaUpgrade").value(false))
                .andExpect(jsonPath("$.mensagem").exists());
    }

    @Test
    @DisplayName("GET /api/alunos/{id}/progresso - Deve mostrar upgrade completo")
    void deveMostrarUpgradeCompleto() throws Exception {
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

        alunoTeste.setPlano(Plano.PREMIUM);
        alunoTeste.setBeneficiosPremiumAtivos(true);

        when(alunoService.buscarPorId(1L)).thenReturn(alunoTeste);

        mockMvc.perform(get("/api/alunos/1/progresso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planoAtual").value("PREMIUM"))
                .andExpect(jsonPath("$.cursosAprovados").value(12))
                .andExpect(jsonPath("$.cursosRestantes").value(0))
                .andExpect(jsonPath("$.percentualProgresso").value(100.0))
                .andExpect(jsonPath("$.elegívelParaUpgrade").value(true));
    }
}
