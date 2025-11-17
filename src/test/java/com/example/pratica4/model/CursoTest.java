package com.example.pratica4.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Curso")
class CursoTest {

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso("Java Básico");
    }

    @Test
    @DisplayName("Construtor com nome deve criar curso corretamente")
    void construtor_com_nome_deve_criar_curso_corretamente() {
        assertEquals("Java Básico", curso.getNome());
        assertEquals(0.0, curso.getMedia());
        assertFalse(curso.isConcluido());
        assertFalse(curso.isAprovado());
    }

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        Curso cursoVazio = new Curso();
        assertNotNull(cursoVazio);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        Curso cursoCompleto = new Curso(1L, "Python", 8.5, true);
        assertEquals(1L, cursoCompleto.getId());
        assertEquals("Python", cursoCompleto.getNome());
        assertEquals(8.5, cursoCompleto.getMedia());
        assertTrue(cursoCompleto.isConcluido());
    }

    @Test
    @DisplayName("Builder deve criar curso corretamente")
    void builder_deve_criar_curso_corretamente() {
        Curso cursoBuilder = Curso.builder()
                .id(10L)
                .nome("Spring Boot")
                .media(9.0)
                .concluido(true)
                .build();

        assertEquals(10L, cursoBuilder.getId());
        assertEquals("Spring Boot", cursoBuilder.getNome());
        assertEquals(9.0, cursoBuilder.getMedia());
        assertTrue(cursoBuilder.isConcluido());
    }

    @Test
    @DisplayName("Deve concluir curso com média")
    void deve_concluir_curso_com_media() {
        curso.concluir(8.5);

        assertTrue(curso.isConcluido());
        assertEquals(8.5, curso.getMedia());
    }

    @Test
    @DisplayName("Deve aprovar curso com média maior ou igual a 7.0")
    void deve_aprovar_curso_com_media_maior_ou_igual_a_7() {
        curso.concluir(7.0);
        assertTrue(curso.isAprovado());

        curso.concluir(7.5);
        assertTrue(curso.isAprovado());

        curso.concluir(10.0);
        assertTrue(curso.isAprovado());
    }

    @Test
    @DisplayName("Deve reprovar curso com média menor que 7.0")
    void deve_reprovar_curso_com_media_menor_que_7() {
        curso.concluir(6.9);
        assertFalse(curso.isAprovado());

        curso.concluir(5.0);
        assertFalse(curso.isAprovado());

        curso.concluir(0.0);
        assertFalse(curso.isAprovado());
    }

    @Test
    @DisplayName("Curso não concluído não deve ser aprovado mesmo com média alta")
    void curso_nao_concluido_nao_deve_ser_aprovado() {
        curso.setMedia(10.0);
        curso.setConcluido(false);

        assertFalse(curso.isAprovado());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        curso.setId(100L);
        curso.setNome("Novo Curso");
        curso.setMedia(7.5);
        curso.setConcluido(true);

        assertEquals(100L, curso.getId());
        assertEquals("Novo Curso", curso.getNome());
        assertEquals(7.5, curso.getMedia());
        assertTrue(curso.isConcluido());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        Curso curso1 = Curso.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .build();

        Curso curso2 = Curso.builder()
                .id(1L)
                .nome("Java")
                .media(8.0)
                .concluido(true)
                .build();

        assertEquals(curso1, curso2);
        assertEquals(curso1.hashCode(), curso2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        String toString = curso.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Java Básico"));
    }

    @Test
    @DisplayName("IsConcluido deve retornar false por padrão")
    void is_concluido_deve_retornar_false_por_padrao() {
        Curso cursoNovo = new Curso("Teste");
        assertFalse(cursoNovo.isConcluido());
    }

    @Test
    @DisplayName("Media deve ser 0.0 por padrão")
    void media_deve_ser_zero_por_padrao() {
        Curso cursoNovo = new Curso("Teste");
        assertEquals(0.0, cursoNovo.getMedia());
    }

    @Test
    @DisplayName("Deve aceitar média no limite exato de 7.0")
    void deve_aceitar_media_no_limite_exato() {
        curso.concluir(7.0);

        assertTrue(curso.isConcluido());
        assertTrue(curso.isAprovado());
        assertEquals(7.0, curso.getMedia());
    }

    @Test
    @DisplayName("Deve aceitar média logo abaixo do limite (6.9)")
    void deve_aceitar_media_logo_abaixo_do_limite() {
        curso.concluir(6.9);

        assertTrue(curso.isConcluido());
        assertFalse(curso.isAprovado());
        assertEquals(6.9, curso.getMedia());
    }

    @Test
    @DisplayName("Deve aceitar média logo acima do limite (7.1)")
    void deve_aceitar_media_logo_acima_do_limite() {
        curso.concluir(7.1);

        assertTrue(curso.isConcluido());
        assertTrue(curso.isAprovado());
        assertEquals(7.1, curso.getMedia());
    }
}
