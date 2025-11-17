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
 * Testes unitários para a classe CPF (Value Object).
 *
 * Testa:
 * - Validação de formato (11 dígitos numéricos)
 * - Formatação (123.456.789-01)
 * - Comportamento com valores válidos e inválidos
 */
@DisplayName("Testes da classe CPF")
class CPFTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar CPF válido com 11 dígitos")
    void deveCriarCpfValido() {
        CPF cpf = new CPF("12345678901");

        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertTrue(violations.isEmpty(), "CPF válido não deve ter violações");
        assertEquals("12345678901", cpf.getNumero());
    }

    @Test
    @DisplayName("Deve formatar CPF corretamente")
    void deveFormatarCpfCorretamente() {
        CPF cpf = new CPF("12345678901");

        assertEquals("123.456.789-01", cpf.getFormatado());
        assertEquals("123.456.789-01", cpf.toString());
    }

    @Test
    @DisplayName("Deve rejeitar CPF com menos de 11 dígitos")
    void deveRejeitarCpfComMenosDe11Digitos() {
        CPF cpf = new CPF("123456789"); // 9 dígitos
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(), "CPF com 9 dígitos deve ser inválido");
    }

    @Test
    @DisplayName("Deve rejeitar CPF com mais de 11 dígitos")
    void deveRejeitarCpfComMaisDe11Digitos() {
        CPF cpf = new CPF("123456789012"); // 12 dígitos
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(), "CPF com 12 dígitos deve ser inválido");
    }

    @Test
    @DisplayName("Deve rejeitar CPF com letras")
    void deveRejeitarCpfComLetras() {
        CPF cpf = new CPF("1234567890a");
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(), "CPF com letras deve ser inválido");
    }

    @Test
    @DisplayName("Deve rejeitar CPF com caracteres especiais")
    void deveRejeitarCpfComCaracteresEspeciais() {
        CPF cpf = new CPF("123.456.789-01"); // Formatado (não aceito no padrão)
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(),
            "CPF formatado deve ser inválido (esperado apenas dígitos)");
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo")
    void deveRejeitarCpfNulo() {
        CPF cpf = new CPF(null);
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(), "CPF nulo deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve rejeitar CPF vazio")
    void deveRejeitarCpfVazio() {
        CPF cpf = new CPF("");
        Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

        assertFalse(violations.isEmpty(), "CPF vazio deve ser rejeitado");
    }

    @Test
    @DisplayName("Formatação deve lidar com CPF inválido gracefully")
    void formatacaoDeveLidarComCpfInvalidoGracefully() {
        CPF cpf1 = new CPF("123"); // Muito curto
        CPF cpf2 = new CPF(null);

        // Não deve lançar exceção, apenas retornar o valor original
        assertNotNull(cpf1.getFormatado());
        assertEquals("123", cpf1.getFormatado());
        assertNull(cpf2.getFormatado());
    }

    @Test
    @DisplayName("CPFs iguais devem ser considerados iguais")
    void cpfsIguaisDevemSerIguais() {
        CPF cpf1 = new CPF("12345678901");
        CPF cpf2 = new CPF("12345678901");

        assertEquals(cpf1, cpf2);
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
    }

    @Test
    @DisplayName("CPFs diferentes devem ser considerados diferentes")
    void cpfsDiferentesDevemSerDiferentes() {
        CPF cpf1 = new CPF("12345678901");
        CPF cpf2 = new CPF("98765432109");

        assertNotEquals(cpf1, cpf2);
    }

    @Test
    @DisplayName("Construtor sem argumentos deve funcionar")
    void construtorSemArgumentosDeveFuncionar() {
        CPF cpf = new CPF();
        assertNotNull(cpf);
    }

    @Test
    @DisplayName("Setter deve funcionar corretamente")
    void setterDeveFuncionarCorretamente() {
        CPF cpf = new CPF();
        cpf.setNumero("12345678901");

        assertEquals("12345678901", cpf.getNumero());
        assertEquals("123.456.789-01", cpf.getFormatado());
    }

    @Test
    @DisplayName("Deve aceitar CPFs válidos conhecidos")
    void deveAceitarCpfsValidosConhecidos() {
        String[] cpfsValidos = {
            "11111111111",
            "00000000000",
            "12345678901",
            "98765432109"
        };

        for (String cpfStr : cpfsValidos) {
            CPF cpf = new CPF(cpfStr);
            Set<ConstraintViolation<CPF>> violations = validator.validate(cpf);

            assertTrue(violations.isEmpty(),
                "CPF '" + cpfStr + "' deveria ser válido (formato)");
        }
    }
}
