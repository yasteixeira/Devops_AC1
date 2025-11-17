package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Plano;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do CreateAlunoDTO")
class CreateAlunoDTOTest {

    private Validator validator;
    private CreateAlunoDTO createAlunoDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        createAlunoDTO = CreateAlunoDTO.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@teste.com")
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        CreateAlunoDTO dto = new CreateAlunoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        CreateAlunoDTO dto = new CreateAlunoDTO("Maria", "98765432109", "maria@teste.com");

        assertEquals("Maria", dto.getNome());
        assertEquals("98765432109", dto.getCpf());
        assertEquals("maria@teste.com", dto.getEmail());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        CreateAlunoDTO dto = CreateAlunoDTO.builder()
                .nome("Pedro Santos")
                .cpf("11111111111")
                .email("pedro@teste.com")
                .build();

        assertEquals("Pedro Santos", dto.getNome());
        assertEquals("11111111111", dto.getCpf());
        assertEquals("pedro@teste.com", dto.getEmail());
    }

    @Test
    @DisplayName("Deve validar DTO com dados válidos")
    void deve_validar_dto_com_dados_validos() {
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome em branco")
    void deve_rejeitar_nome_em_branco() {
        createAlunoDTO.setNome("");
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome null")
    void deve_rejeitar_nome_null() {
        createAlunoDTO.setNome(null);
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar CPF inválido")
    void deve_rejeitar_cpf_invalido() {
        createAlunoDTO.setCpf("123"); // Menos de 11 dígitos
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar CPF null")
    void deve_rejeitar_cpf_null() {
        createAlunoDTO.setCpf(null);
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar CPF com letras")
    void deve_rejeitar_cpf_com_letras() {
        createAlunoDTO.setCpf("1234567890a");
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar email inválido")
    void deve_rejeitar_email_invalido() {
        createAlunoDTO.setEmail("email-invalido");
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar email null")
    void deve_rejeitar_email_null() {
        createAlunoDTO.setEmail(null);
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar email em branco")
    void deve_rejeitar_email_em_branco() {
        createAlunoDTO.setEmail("");
        Set<ConstraintViolation<CreateAlunoDTO>> violations = validator.validate(createAlunoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("ToEntity deve converter DTO para Aluno com plano BASICO")
    void to_entity_deve_converter_dto_para_aluno() {
        Aluno resultado = createAlunoDTO.toEntity();

        assertNotNull(resultado);
        assertEquals(createAlunoDTO.getNome(), resultado.getNome());
        assertEquals(createAlunoDTO.getCpf(), resultado.getCpf().getNumero());
        assertEquals(createAlunoDTO.getEmail(), resultado.getEmail().getEndereco());
        assertEquals(Plano.BASICO, resultado.getPlano());
        assertNotNull(resultado.getDataCadastro());
        assertFalse(resultado.isBeneficiosPremiumAtivos());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        createAlunoDTO.setNome("Novo Nome");
        createAlunoDTO.setCpf("11111111111");
        createAlunoDTO.setEmail("novo@email.com");

        assertEquals("Novo Nome", createAlunoDTO.getNome());
        assertEquals("11111111111", createAlunoDTO.getCpf());
        assertEquals("novo@email.com", createAlunoDTO.getEmail());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        CreateAlunoDTO dto1 = CreateAlunoDTO.builder()
                .nome("João")
                .cpf("12345678901")
                .email("joao@teste.com")
                .build();

        CreateAlunoDTO dto2 = CreateAlunoDTO.builder()
                .nome("João")
                .cpf("12345678901")
                .email("joao@teste.com")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = createAlunoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("João Silva"));
    }
}
