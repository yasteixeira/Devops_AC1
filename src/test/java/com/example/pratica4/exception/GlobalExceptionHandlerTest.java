package com.example.pratica4.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Testes do GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve tratar EntityNotFoundException retornando 404")
    void deve_tratar_entity_not_found_exception() {
        EntityNotFoundException exception = new EntityNotFoundException("Aluno não encontrado com ID: 999");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso não encontrado", response.getBody().getError());
        assertEquals("Aluno não encontrado com ID: 999", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException retornando 400")
    void deve_tratar_illegal_argument_exception() {
        IllegalArgumentException exception = new IllegalArgumentException("CPF já cadastrado: 12345678901");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Argumento inválido", response.getBody().getError());
        assertEquals("CPF já cadastrado: 12345678901", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com erros de validação")
    void deve_tratar_method_argument_not_valid_exception() {
        // Mock do MethodArgumentNotValidException
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("createAlunoDTO", "cpf", "CPF deve conter exatamente 11 dígitos numéricos");
        FieldError fieldError2 = new FieldError("createAlunoDTO", "email", "Formato de e-mail inválido");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro de validação", response.getBody().getError());
        assertEquals("Dados inválidos fornecidos", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
        assertNotNull(response.getBody().getDetails());
        assertEquals(2, response.getBody().getDetails().size());
        assertTrue(response.getBody().getDetails().containsKey("cpf"));
        assertTrue(response.getBody().getDetails().containsKey("email"));
    }

    @Test
    @DisplayName("Deve tratar Exception genérica retornando 500")
    void deve_tratar_exception_generica() {
        Exception exception = new Exception("Erro inesperado no sistema");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Erro interno do servidor", response.getBody().getError());
        assertEquals("Ocorreu um erro inesperado. Por favor, contate o suporte.", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Deve tratar NullPointerException como erro genérico")
    void deve_tratar_null_pointer_exception() {
        NullPointerException exception = new NullPointerException("Objeto nulo");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    @DisplayName("Deve tratar RuntimeException como erro genérico")
    void deve_tratar_runtime_exception() {
        RuntimeException exception = new RuntimeException("Erro de runtime");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    @DisplayName("ErrorResponse de validação deve incluir detalhes dos campos")
    void error_response_de_validacao_deve_incluir_detalhes() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("dto", "nome", "Nome é obrigatório");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(exception);

        assertNotNull(response.getBody().getDetails());
        assertEquals("Nome é obrigatório", response.getBody().getDetails().get("nome"));
    }

    @Test
    @DisplayName("Deve criar ErrorResponse com timestamp atual")
    void deve_criar_error_response_com_timestamp_atual() {
        EntityNotFoundException exception = new EntityNotFoundException("Teste");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(exception);

        assertNotNull(response.getBody().getTimestamp());
        assertTrue(response.getBody().getTimestamp().isBefore(java.time.LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("ErrorResponse de EntityNotFoundException não deve ter details")
    void error_response_entity_not_found_nao_deve_ter_details() {
        EntityNotFoundException exception = new EntityNotFoundException("Teste");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(exception);

        assertNull(response.getBody().getDetails());
    }

    @Test
    @DisplayName("ErrorResponse de IllegalArgumentException não deve ter details")
    void error_response_illegal_argument_nao_deve_ter_details() {
        IllegalArgumentException exception = new IllegalArgumentException("Teste");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(exception);

        assertNull(response.getBody().getDetails());
    }

    @Test
    @DisplayName("ErrorResponse genérico não deve ter details")
    void error_response_generico_nao_deve_ter_details() {
        Exception exception = new Exception("Teste");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNull(response.getBody().getDetails());
    }
}
