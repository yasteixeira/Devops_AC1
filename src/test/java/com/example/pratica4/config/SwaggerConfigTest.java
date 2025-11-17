package com.example.pratica4.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do SwaggerConfig")
class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    @DisplayName("Deve criar bean OpenAPI")
    void deve_criar_bean_open_api() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
    }

    @Test
    @DisplayName("OpenAPI deve ter informações configuradas")
    void open_api_deve_ter_informacoes_configuradas() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI.getInfo());
    }

    @Test
    @DisplayName("Deve ter título configurado")
    void deve_ter_titulo_configurado() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info.getTitle());
        assertTrue(info.getTitle().contains("API"));
        assertTrue(info.getTitle().contains("Alunos") || info.getTitle().contains("Pratica4"));
    }

    @Test
    @DisplayName("Deve ter versão configurada")
    void deve_ter_versao_configurada() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info.getVersion());
        assertEquals("1.0.0", info.getVersion());
    }

    @Test
    @DisplayName("Deve ter descrição configurada")
    void deve_ter_descricao_configurada() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info.getDescription());
        assertFalse(info.getDescription().isEmpty());
        assertTrue(info.getDescription().contains("API"));
    }

    @Test
    @DisplayName("Deve ter contato configurado")
    void deve_ter_contato_configurado() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info.getContact());
        assertNotNull(info.getContact().getName());
        assertNotNull(info.getContact().getEmail());
    }

    @Test
    @DisplayName("Contato deve ter nome correto")
    void contato_deve_ter_nome_correto() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertEquals("Equipe Pratica4", openAPI.getInfo().getContact().getName());
    }

    @Test
    @DisplayName("Contato deve ter email correto")
    void contato_deve_ter_email_correto() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertEquals("contato@pratica4.com", openAPI.getInfo().getContact().getEmail());
    }

    @Test
    @DisplayName("Deve ter licença configurada")
    void deve_ter_licenca_configurada() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info.getLicense());
        assertNotNull(info.getLicense().getName());
        assertNotNull(info.getLicense().getUrl());
    }

    @Test
    @DisplayName("Licença deve ser Apache 2.0")
    void licenca_deve_ser_apache_2() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertEquals("Apache 2.0", openAPI.getInfo().getLicense().getName());
        assertTrue(openAPI.getInfo().getLicense().getUrl().contains("apache.org"));
    }

    @Test
    @DisplayName("Descrição deve mencionar funcionalidades principais")
    void descricao_deve_mencionar_funcionalidades() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String descricao = openAPI.getInfo().getDescription();

        assertTrue(descricao.contains("aluno") || descricao.contains("Aluno"));
        assertTrue(descricao.contains("curso") || descricao.contains("Curso"));
    }

    @Test
    @DisplayName("Descrição deve mencionar upgrade de plano")
    void descricao_deve_mencionar_upgrade_plano() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String descricao = openAPI.getInfo().getDescription();

        assertTrue(descricao.contains("upgrade") || descricao.contains("PREMIUM") || descricao.contains("plano"));
    }

    @Test
    @DisplayName("Descrição deve mencionar regra de negócio")
    void descricao_deve_mencionar_regra_negocio() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String descricao = openAPI.getInfo().getDescription();

        assertTrue(descricao.contains("12") || descricao.contains("doze"));
        assertTrue(descricao.contains("7.0") || descricao.contains("sete"));
    }

    @Test
    @DisplayName("SwaggerConfig deve ser instanciável")
    void swagger_config_deve_ser_instanciavel() {
        SwaggerConfig config = new SwaggerConfig();
        assertNotNull(config);
    }

    @Test
    @DisplayName("Chamadas múltiplas devem retornar objetos diferentes")
    void chamadas_multiplas_devem_retornar_objetos_diferentes() {
        OpenAPI openAPI1 = swaggerConfig.customOpenAPI();
        OpenAPI openAPI2 = swaggerConfig.customOpenAPI();

        assertNotSame(openAPI1, openAPI2);
    }

    @Test
    @DisplayName("Configuração deve ter título completo")
    void configuracao_deve_ter_titulo_completo() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertEquals("Pratica4 - API de Gerenciamento de Alunos", openAPI.getInfo().getTitle());
    }
}
