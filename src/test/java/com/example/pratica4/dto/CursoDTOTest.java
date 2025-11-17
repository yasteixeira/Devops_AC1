package com.example.pratica4.dto;

import com.example.pratica4.model.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do CursoDTO")
class CursoDTOTest {

    private Curso curso;
    private CursoDTO cursoDTO;

    @BeforeEach
    void setUp() {
        curso = Curso.builder()
                .id(1L)
                .nome("Java Básico")
                .media(8.5)
                .concluido(true)
                .build();

        cursoDTO = CursoDTO.builder()
                .id(1L)
                .nome("Java Básico")
                .media(8.5)
                .concluido(true)
                .aprovado(true)
                .build();
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        CursoDTO dto = new CursoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        CursoDTO dto = new CursoDTO(1L, "Python", 9.0, true, true);

        assertEquals(1L, dto.getId());
        assertEquals("Python", dto.getNome());
        assertEquals(9.0, dto.getMedia());
        assertTrue(dto.isConcluido());
        assertTrue(dto.isAprovado());
    }

    @Test
    @DisplayName("Builder deve criar DTO corretamente")
    void builder_deve_criar_dto_corretamente() {
        CursoDTO dto = CursoDTO.builder()
                .id(10L)
                .nome("Spring Boot")
                .media(7.5)
                .concluido(true)
                .aprovado(true)
                .build();

        assertEquals(10L, dto.getId());
        assertEquals("Spring Boot", dto.getNome());
        assertEquals(7.5, dto.getMedia());
        assertTrue(dto.isConcluido());
        assertTrue(dto.isAprovado());
    }

    @Test
    @DisplayName("FromEntity deve converter Curso para CursoDTO")
    void from_entity_deve_converter_curso_para_dto() {
        CursoDTO resultado = CursoDTO.fromEntity(curso);

        assertNotNull(resultado);
        assertEquals(curso.getId(), resultado.getId());
        assertEquals(curso.getNome(), resultado.getNome());
        assertEquals(curso.getMedia(), resultado.getMedia());
        assertEquals(curso.isConcluido(), resultado.isConcluido());
        assertEquals(curso.isAprovado(), resultado.isAprovado());
    }

    @Test
    @DisplayName("FromEntity deve retornar null quando curso é null")
    void from_entity_deve_retornar_null_quando_curso_null() {
        CursoDTO resultado = CursoDTO.fromEntity(null);
        assertNull(resultado);
    }

    @Test
    @DisplayName("FromEntity deve marcar aprovado corretamente para curso aprovado")
    void from_entity_deve_marcar_aprovado_corretamente() {
        curso.concluir(7.0);
        CursoDTO resultado = CursoDTO.fromEntity(curso);

        assertTrue(resultado.isAprovado());
        assertTrue(resultado.isConcluido());
        assertEquals(7.0, resultado.getMedia());
    }

    @Test
    @DisplayName("FromEntity deve marcar reprovado corretamente para curso reprovado")
    void from_entity_deve_marcar_reprovado_corretamente() {
        curso.concluir(6.5);
        CursoDTO resultado = CursoDTO.fromEntity(curso);

        assertFalse(resultado.isAprovado());
        assertTrue(resultado.isConcluido());
        assertEquals(6.5, resultado.getMedia());
    }

    @Test
    @DisplayName("ToEntity deve converter CursoDTO para Curso")
    void to_entity_deve_converter_dto_para_curso() {
        Curso resultado = cursoDTO.toEntity();

        assertNotNull(resultado);
        assertEquals(cursoDTO.getId(), resultado.getId());
        assertEquals(cursoDTO.getNome(), resultado.getNome());
        assertEquals(cursoDTO.getMedia(), resultado.getMedia());
        assertEquals(cursoDTO.isConcluido(), resultado.isConcluido());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        cursoDTO.setId(100L);
        cursoDTO.setNome("Novo Curso");
        cursoDTO.setMedia(9.5);
        cursoDTO.setConcluido(false);
        cursoDTO.setAprovado(false);

        assertEquals(100L, cursoDTO.getId());
        assertEquals("Novo Curso", cursoDTO.getNome());
        assertEquals(9.5, cursoDTO.getMedia());
        assertFalse(cursoDTO.isConcluido());
        assertFalse(cursoDTO.isAprovado());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        CursoDTO dto1 = CursoDTO.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .aprovado(true)
                .build();

        CursoDTO dto2 = CursoDTO.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .aprovado(true)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = cursoDTO.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Java Básico"));
    }
}
