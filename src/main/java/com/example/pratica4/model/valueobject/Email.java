package com.example.pratica4.model.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Value Object que representa um e-mail válido.
 *
 * Em DDD (Domain-Driven Design), Value Objects são objetos imutáveis
 * que representam conceitos do domínio e possuem validação própria.
 *
 * @Embeddable - Indica que este objeto será incorporado em outras entidades (não terá tabela própria)
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

    /**
     * Regex para validar formato de e-mail
     * Exemplo válido: usuario@exemplo.com
     */
    @NotBlank(message = "Email não pode estar vazio")
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Formato de e-mail inválido"
    )
    private String endereco;

    @Override
    public String toString() {
        return this.endereco;
    }
}
