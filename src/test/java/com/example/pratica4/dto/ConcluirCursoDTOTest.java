package com.example.pratica4.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ConcluirCursoDTO")
class ConcluirCursoDTOTest {

    private Validator validator;
    private ConcluirCursoDTO concluirCursoDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        concluirCursoDTO = ConcluirCursoDTO.builder()
                .media(8.5)
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        ConcluirCursoDTO dto = new ConcluirCursoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        ConcluirCursoDTO dto = new ConcluirCursoDTO(7.5);
        assertEquals(7.5, dto.getMedia());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        ConcluirCursoDTO dto = ConcluirCursoDTO.builder()
                .media(9.0)
                .build();

        assertEquals(9.0, dto.getMedia());
    }

    @Test
    @DisplayName("Deve validar DTO com média válida")
    void deve_validar_dto_com_media_valida() {
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar média 0.0")
    void deve_aceitar_media_zero() {
        concluirCursoDTO.setMedia(0.0);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar média 10.0")
    void deve_aceitar_media_dez() {
        concluirCursoDTO.setMedia(10.0);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar média null")
    void deve_rejeitar_media_null() {
        concluirCursoDTO.setMedia(null);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar média menor que 0.0")
    void deve_rejeitar_media_menor_que_zero() {
        concluirCursoDTO.setMedia(-0.1);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar média maior que 10.0")
    void deve_rejeitar_media_maior_que_dez() {
        concluirCursoDTO.setMedia(10.1);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar média 7.0 (limite de aprovação)")
    void deve_aceitar_media_sete() {
        concluirCursoDTO.setMedia(7.0);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar média 6.9")
    void deve_aceitar_media_seis_nove() {
        concluirCursoDTO.setMedia(6.9);
        Set<ConstraintViolation<ConcluirCursoDTO>> violations = validator.validate(concluirCursoDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        concluirCursoDTO.setMedia(5.5);
        assertEquals(5.5, concluirCursoDTO.getMedia());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        ConcluirCursoDTO dto1 = ConcluirCursoDTO.builder()
                .media(8.0)
                .build();

        ConcluirCursoDTO dto2 = ConcluirCursoDTO.builder()
                .media(8.0)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = concluirCursoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("8.5") || toString.contains("media"));
    }
}
