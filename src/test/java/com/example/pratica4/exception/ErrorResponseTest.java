package com.example.pratica4.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ErrorResponse")
class ErrorResponseTest {

    @Test
    @DisplayName("Construtor NoArgs deve funcionar")
    void construtor_no_args_deve_funcionar() {
        ErrorResponse errorResponse = new ErrorResponse();
        assertNotNull(errorResponse);
    }

    @Test
    @DisplayName("Construtor AllArgs deve funcionar")
    void construtor_all_args_deve_funcionar() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("campo", "erro");

        ErrorResponse errorResponse = new ErrorResponse(
                now,
                400,
                "Bad Request",
                "Dados inválidos",
                details
        );

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Dados inválidos", errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    @DisplayName("Builder deve criar ErrorResponse corretamente")
    void builder_deve_criar_error_response_corretamente() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("cpf", "CPF inválido");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("Recurso não encontrado")
                .details(details)
                .build();

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Recurso não encontrado", errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    @DisplayName("Getters e Setters devem funcionar")
    void getters_e_setters_devem_funcionar() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("email", "Email inválido");

        errorResponse.setTimestamp(now);
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("Erro interno");
        errorResponse.setDetails(details);

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("Erro interno", errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    @DisplayName("Deve aceitar details null")
    void deve_aceitar_details_null() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("Recurso não encontrado")
                .details(null)
                .build();

        assertNull(errorResponse.getDetails());
    }

    @Test
    @DisplayName("Deve aceitar details vazio")
    void deve_aceitar_details_vazio() {
        Map<String, String> details = new HashMap<>();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Erro de validação")
                .details(details)
                .build();

        assertNotNull(errorResponse.getDetails());
        assertTrue(errorResponse.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Equals e HashCode devem funcionar")
    void equals_e_hashcode_devem_funcionar() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse error1 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Erro")
                .build();

        ErrorResponse error2 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Erro")
                .build();

        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    @DisplayName("ToString deve funcionar")
    void toString_deve_funcionar() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("Recurso não encontrado")
                .build();

        String toString = errorResponse.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("404") || toString.contains("Not Found"));
    }

    @Test
    @DisplayName("Deve criar ErrorResponse com múltiplos detalhes")
    void deve_criar_error_response_com_multiplos_detalhes() {
        Map<String, String> details = new HashMap<>();
        details.put("nome", "Nome é obrigatório");
        details.put("cpf", "CPF inválido");
        details.put("email", "Email inválido");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Erro de validação")
                .message("Múltiplos campos inválidos")
                .details(details)
                .build();

        assertEquals(3, errorResponse.getDetails().size());
        assertTrue(errorResponse.getDetails().containsKey("nome"));
        assertTrue(errorResponse.getDetails().containsKey("cpf"));
        assertTrue(errorResponse.getDetails().containsKey("email"));
    }
}
