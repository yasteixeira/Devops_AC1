package com.example.pratica4.model.valueobject;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Email (Value Object).
 *
 * Testa:
 * - Validação de formato de e-mail
 * - Comportamento com valores válidos e inválidos
 * - Métodos toString, equals, hashCode
 */
@DisplayName("Testes da classe Email")
class EmailTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Inicializa validador Bean Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar email válido com sucesso")
    void deveCriarEmailValido() {
        Email email = new Email("usuario@exemplo.com");

        Set<ConstraintViolation<Email>> violations = validator.validate(email);

        assertTrue(violations.isEmpty(), "Email válido não deve ter violações");
        assertEquals("usuario@exemplo.com", email.getEndereco());
    }

    @Test
    @DisplayName("Deve validar emails em diferentes formatos válidos")
    void deveValidarEmailsValidos() {
        String[] emailsValidos = {
            "teste@teste.com",
            "usuario.nome@empresa.com.br",
            "user123@example.org",
            "first.last@subdomain.example.com",
            "user+tag@gmail.com"
        };

        for (String emailStr : emailsValidos) {
            Email email = new Email(emailStr);
            Set<ConstraintViolation<Email>> violations = validator.validate(email);

            assertTrue(violations.isEmpty(),
                "Email '" + emailStr + "' deveria ser válido");
        }
    }

    @Test
    @DisplayName("Deve rejeitar emails inválidos")
    void deveRejeitarEmailsInvalidos() {
        String[] emailsInvalidos = {
            "email-sem-arroba.com",
            "@sem-usuario.com",
            "sem-dominio@",
            "sem-ponto@dominio",
            "espacos @email.com",
            "duplo@@email.com",
            ""
        };

        for (String emailStr : emailsInvalidos) {
            Email email = new Email(emailStr);
            Set<ConstraintViolation<Email>> violations = validator.validate(email);

            assertFalse(violations.isEmpty(),
                "Email '" + emailStr + "' deveria ser inválido");
        }
    }

    @Test
    @DisplayName("Deve rejeitar email nulo")
    void deveRejeitarEmailNulo() {
        Email email = new Email(null);
        Set<ConstraintViolation<Email>> violations = validator.validate(email);

        assertFalse(violations.isEmpty(), "Email nulo deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve rejeitar email vazio")
    void deveRejeitarEmailVazio() {
        Email email = new Email("");
        Set<ConstraintViolation<Email>> violations = validator.validate(email);

        assertFalse(violations.isEmpty(), "Email vazio deve ser rejeitado");
    }

    @Test
    @DisplayName("Método toString deve retornar endereço")
    void toStringDeveRetornarEndereco() {
        Email email = new Email("teste@teste.com");

        assertEquals("teste@teste.com", email.toString());
    }

    @Test
    @DisplayName("Emails iguais devem ser considerados iguais")
    void emailsIguaisDevemSerIguais() {
        Email email1 = new Email("teste@teste.com");
        Email email2 = new Email("teste@teste.com");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    @DisplayName("Emails diferentes devem ser considerados diferentes")
    void emailsDiferentesDevemSerDiferentes() {
        Email email1 = new Email("teste1@teste.com");
        Email email2 = new Email("teste2@teste.com");

        assertNotEquals(email1, email2);
    }

    @Test
    @DisplayName("Construtor sem argumentos deve funcionar")
    void construtorSemArgumentosDeveFuncionar() {
        Email email = new Email();
        assertNotNull(email);
    }

    @Test
    @DisplayName("Setter deve funcionar corretamente")
    void setterDeveFuncionarCorretamente() {
        Email email = new Email();
        email.setEndereco("novo@email.com");

        assertEquals("novo@email.com", email.getEndereco());
    }
}
