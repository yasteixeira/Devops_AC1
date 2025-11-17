package com.example.pratica4.dto;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para criação de novo aluno.
 *
 * Contém apenas os campos necessários para criar um aluno.
 * Validações são aplicadas aqui (Bean Validation).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAlunoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @jakarta.validation.constraints.Email(message = "Formato de e-mail inválido")
    private String email;

    /**
     * Converte este DTO em uma entidade Aluno
     * Novo aluno sempre começa como BASICO
     */
    public Aluno toEntity() {
        return Aluno.builder()
                .nome(this.nome)
                .cpf(new CPF(this.cpf))
                .email(new Email(this.email))
                .plano(Plano.BASICO)
                .dataCadastro(LocalDate.now())
                .beneficiosPremiumAtivos(false)
                .build();
    }
}
