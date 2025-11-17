package com.example.pratica4.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Classe para padronizar respostas de erro da API.
 *
 * Exemplo de resposta JSON:
 * {
 *   "timestamp": "2025-11-17T10:30:00",
 *   "status": 400,
 *   "error": "Erro de validação",
 *   "message": "Dados inválidos fornecidos",
 *   "details": {
 *     "cpf": "CPF deve conter exatamente 11 dígitos numéricos",
 *     "email": "Formato de e-mail inválido"
 *   }
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * Data/hora do erro
     */
    private LocalDateTime timestamp;

    /**
     * Código HTTP do erro (400, 404, 500, etc)
     */
    private int status;

    /**
     * Título resumido do erro
     */
    private String error;

    /**
     * Mensagem descritiva do erro
     */
    private String message;

    /**
     * Detalhes adicionais (opcional)
     * Usado principalmente para erros de validação
     */
    private Map<String, String> details;
}
