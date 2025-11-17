package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do AlunoDTO")
class AlunoDTOTest {

    private Aluno aluno;
    private AlunoDTO alunoDTO;

    @BeforeEach
    void setUp() {
        aluno = Aluno.builder()
                .id(1L)
                .nome("João Silva")
                .cpf(new CPF("12345678901"))
                .email(new Email("joao@teste.com"))
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.of(2024, 1, 1))
                .beneficiosPremiumAtivos(false)
                .build();

        alunoDTO = AlunoDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("123.456.789-01")
                .email("joao@teste.com")
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.of(2024, 1, 1))
                .beneficiosPremiumAtivos(false)
                .quantidadeCursosAprovados(0)
                .cursos(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        AlunoDTO dto = new AlunoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        AlunoDTO dto = new AlunoDTO(
                1L,
                "Maria",
                "12345678901",
                "maria@teste.com",
                Plano.PREMIUM,
                LocalDate.now(),
                true,
                12,
                new ArrayList<>()
        );

        assertEquals(1L, dto.getId());
        assertEquals("Maria", dto.getNome());
        assertEquals(Plano.PREMIUM, dto.getPlano());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        AlunoDTO dto = AlunoDTO.builder()
                .id(10L)
                .nome("Pedro")
                .cpf("98765432109")
                .email("pedro@teste.com")
                .plano(Plano.PREMIUM)
                .dataCadastro(LocalDate.of(2023, 1, 1))
                .beneficiosPremiumAtivos(true)
                .quantidadeCursosAprovados(15)
                .cursos(new ArrayList<>())
                .build();

        assertEquals(10L, dto.getId());
        assertEquals("Pedro", dto.getNome());
        assertEquals("98765432109", dto.getCpf());
        assertEquals("pedro@teste.com", dto.getEmail());
        assertEquals(Plano.PREMIUM, dto.getPlano());
        assertTrue(dto.isBeneficiosPremiumAtivos());
        assertEquals(15, dto.getQuantidadeCursosAprovados());
    }

    @Test
    @DisplayName("FromEntity deve converter Aluno para AlunoDTO")
    void from_entity_deve_converter_aluno_para_dto() {
        AlunoDTO resultado = AlunoDTO.fromEntity(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        assertEquals(aluno.getNome(), resultado.getNome());
        assertEquals("123.456.789-01", resultado.getCpf()); // Formatado
        assertEquals("joao@teste.com", resultado.getEmail());
        assertEquals(aluno.getPlano(), resultado.getPlano());
        assertEquals(aluno.getDataCadastro(), resultado.getDataCadastro());
        assertEquals(aluno.isBeneficiosPremiumAtivos(), resultado.isBeneficiosPremiumAtivos());
        assertNotNull(resultado.getCursos());
    }

    @Test
    @DisplayName("FromEntity deve retornar null quando aluno é null")
    void from_entity_deve_retornar_null_quando_aluno_null() {
        AlunoDTO resultado = AlunoDTO.fromEntity(null);
        assertNull(resultado);
    }

    @Test
    @DisplayName("FromEntity deve converter aluno com cursos")
    void from_entity_deve_converter_aluno_com_cursos() {
        Curso curso1 = Curso.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .build();

        Curso curso2 = Curso.builder()
                .id(2L)
                .nome("Python")
                .media(7.5)
                .concluido(true)
                .build();

        aluno.adicionarCurso(curso1);
        aluno.adicionarCurso(curso2);

        AlunoDTO resultado = AlunoDTO.fromEntity(aluno);

        assertNotNull(resultado.getCursos());
        assertEquals(2, resultado.getCursos().size());
        assertEquals(2, resultado.getQuantidadeCursosAprovados());
    }

    @Test
    @DisplayName("FromEntity deve lidar com CPF null")
    void from_entity_deve_lidar_com_cpf_null() {
        aluno.setCpf(null);
        AlunoDTO resultado = AlunoDTO.fromEntity(aluno);

        assertNotNull(resultado);
        assertNull(resultado.getCpf());
    }

    @Test
    @DisplayName("FromEntity deve lidar com Email null")
    void from_entity_deve_lidar_com_email_null() {
        aluno.setEmail(null);
        AlunoDTO resultado = AlunoDTO.fromEntity(aluno);

        assertNotNull(resultado);
        assertNull(resultado.getEmail());
    }

    @Test
    @DisplayName("ToEntity deve converter AlunoDTO para Aluno")
    void to_entity_deve_converter_dto_para_aluno() {
        Aluno resultado = alunoDTO.toEntity();

        assertNotNull(resultado);
        assertEquals(alunoDTO.getId(), resultado.getId());
        assertEquals(alunoDTO.getNome(), resultado.getNome());
        assertEquals(alunoDTO.getPlano(), resultado.getPlano());
        assertEquals(alunoDTO.getDataCadastro(), resultado.getDataCadastro());
        assertEquals(alunoDTO.isBeneficiosPremiumAtivos(), resultado.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("ToEntity deve converter cursos quando existem")
    void to_entity_deve_converter_cursos_quando_existem() {
        List<CursoDTO> cursos = new ArrayList<>();
        cursos.add(CursoDTO.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .aprovado(true)
                .build());

        alunoDTO.setCursos(cursos);

        Aluno resultado = alunoDTO.toEntity();

        assertNotNull(resultado.getCursos());
        assertEquals(1, resultado.getCursos().size());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        alunoDTO.setId(100L);
        alunoDTO.setNome("Novo Nome");
        alunoDTO.setCpf("11111111111");
        alunoDTO.setEmail("novo@email.com");
        alunoDTO.setPlano(Plano.PREMIUM);
        alunoDTO.setDataCadastro(LocalDate.of(2025, 1, 1));
        alunoDTO.setBeneficiosPremiumAtivos(true);
        alunoDTO.setQuantidadeCursosAprovados(20);
        alunoDTO.setCursos(new ArrayList<>());

        assertEquals(100L, alunoDTO.getId());
        assertEquals("Novo Nome", alunoDTO.getNome());
        assertEquals("11111111111", alunoDTO.getCpf());
        assertEquals("novo@email.com", alunoDTO.getEmail());
        assertEquals(Plano.PREMIUM, alunoDTO.getPlano());
        assertEquals(LocalDate.of(2025, 1, 1), alunoDTO.getDataCadastro());
        assertTrue(alunoDTO.isBeneficiosPremiumAtivos());
        assertEquals(20, alunoDTO.getQuantidadeCursosAprovados());
        assertNotNull(alunoDTO.getCursos());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        AlunoDTO dto1 = AlunoDTO.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .email("joao@teste.com")
                .plano(Plano.BASICO)
                .build();

        AlunoDTO dto2 = AlunoDTO.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .email("joao@teste.com")
                .plano(Plano.BASICO)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = alunoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("João Silva"));
    }
}
