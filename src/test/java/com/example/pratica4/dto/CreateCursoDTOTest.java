package com.example.pratica4.dto;

import com.example.pratica4.model.Curso;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do CreateCursoDTO")
class CreateCursoDTOTest {

    private Validator validator;
    private CreateCursoDTO createCursoDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        createCursoDTO = CreateCursoDTO.builder()
                .nome("Java Básico")
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        CreateCursoDTO dto = new CreateCursoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        CreateCursoDTO dto = new CreateCursoDTO("Python Avançado");
        assertEquals("Python Avançado", dto.getNome());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        CreateCursoDTO dto = CreateCursoDTO.builder()
                .nome("Spring Boot")
                .build();

        assertEquals("Spring Boot", dto.getNome());
    }

    @Test
    @DisplayName("Deve validar DTO com nome válido")
    void deve_validar_dto_com_nome_valido() {
        Set<ConstraintViolation<CreateCursoDTO>> violations = validator.validate(createCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome em branco")
    void deve_rejeitar_nome_em_branco() {
        createCursoDTO.setNome("");
        Set<ConstraintViolation<CreateCursoDTO>> violations = validator.validate(createCursoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome null")
    void deve_rejeitar_nome_null() {
        createCursoDTO.setNome(null);
        Set<ConstraintViolation<CreateCursoDTO>> violations = validator.validate(createCursoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("ToEntity deve converter DTO para Curso")
    void to_entity_deve_converter_dto_para_curso() {
        Curso resultado = createCursoDTO.toEntity();

        assertNotNull(resultado);
        assertEquals(createCursoDTO.getNome(), resultado.getNome());
        assertFalse(resultado.isConcluido());
        assertEquals(0.0, resultado.getMedia());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        createCursoDTO.setNome("Novo Curso");
        assertEquals("Novo Curso", createCursoDTO.getNome());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        CreateCursoDTO dto1 = CreateCursoDTO.builder()
                .nome("Java")
                .build();

        CreateCursoDTO dto2 = CreateCursoDTO.builder()
                .nome("Java")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = createCursoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Java Básico"));
    }
}
