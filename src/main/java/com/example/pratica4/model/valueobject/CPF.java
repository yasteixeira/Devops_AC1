package com.example.pratica4.model.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Value Object que representa um CPF válido.
 *
 * Valida o formato do CPF (apenas números, 11 dígitos).
 * Para simplificação, não valida dígitos verificadores nesta versão.
 *
 * @Embeddable - Este objeto será incorporado na entidade Aluno
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPF implements Serializable {

    /**
     * Regex que aceita CPF com 11 dígitos numéricos
     * Exemplo válido: 12345678901
     */
    @NotBlank(message = "CPF não pode estar vazio")
    @Pattern(
        regexp = "^\\d{11}$",
        message = "CPF deve conter exatamente 11 dígitos numéricos"
    )
    private String numero;

    /**
     * Formata o CPF para exibição: 123.456.789-01
     */
    public String getFormatado() {
        if (numero == null || numero.length() != 11) {
            return numero;
        }
        return String.format("%s.%s.%s-%s",
            numero.substring(0, 3),
            numero.substring(3, 6),
            numero.substring(6, 9),
            numero.substring(9, 11)
        );
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
