package com.example.pratica4.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 *
 * Após rodar a aplicação, acesse:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - API Docs JSON: http://localhost:8080/v3/api-docs
 *
 * O Swagger UI permite testar os endpoints diretamente pelo navegador!
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pratica4 - API de Gerenciamento de Alunos")
                        .version("1.0.0")
                        .description(
                                "API REST para gerenciar alunos e sistema de upgrade automático de plano.\n\n" +
                                "**Funcionalidades principais:**\n" +
                                "- Cadastro e gestão de alunos (CRUD completo)\n" +
                                "- Adição de cursos aos alunos\n" +
                                "- Conclusão de cursos com registro de média\n" +
                                "- Upgrade automático de plano BASICO → PREMIUM após 12 cursos aprovados (média >= 7.0)\n" +
                                "- Consulta de progresso do aluno\n\n" +
                                "**Regra de negócio:**\n" +
                                "Aluno começa no plano BASICO. Ao completar 12 cursos com média >= 7.0, " +
                                "o sistema automaticamente faz upgrade para o plano PREMIUM e ativa benefícios premium."
                        )
                        .contact(new Contact()
                                .name("Equipe Pratica4")
                                .email("contato@pratica4.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }
}
