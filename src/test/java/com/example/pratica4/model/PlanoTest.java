package com.example.pratica4.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do enum Plano")
class PlanoTest {

    @Test
    @DisplayName("Enum deve ter valor BASICO")
    void enum_deve_ter_valor_basico() {
        Plano plano = Plano.BASICO;
        assertNotNull(plano);
        assertEquals(Plano.BASICO, plano);
    }

    @Test
    @DisplayName("Enum deve ter valor PREMIUM")
    void enum_deve_ter_valor_premium() {
        Plano plano = Plano.PREMIUM;
        assertNotNull(plano);
        assertEquals(Plano.PREMIUM, plano);
    }

    @Test
    @DisplayName("BASICO deve ter nome 'Básico'")
    void basico_deve_ter_nome_correto() {
        assertEquals("Básico", Plano.BASICO.getNome());
    }

    @Test
    @DisplayName("PREMIUM deve ter nome 'Premium'")
    void premium_deve_ter_nome_correto() {
        assertEquals("Premium", Plano.PREMIUM.getNome());
    }

    @Test
    @DisplayName("Deve ter exatamente 2 valores")
    void deve_ter_exatamente_dois_valores() {
        Plano[] valores = Plano.values();
        assertEquals(2, valores.length);
    }

    @Test
    @DisplayName("Values deve retornar BASICO e PREMIUM")
    void values_deve_retornar_basico_e_premium() {
        Plano[] valores = Plano.values();
        assertEquals(Plano.BASICO, valores[0]);
        assertEquals(Plano.PREMIUM, valores[1]);
    }

    @Test
    @DisplayName("ValueOf deve retornar BASICO")
    void value_of_deve_retornar_basico() {
        Plano plano = Plano.valueOf("BASICO");
        assertEquals(Plano.BASICO, plano);
    }

    @Test
    @DisplayName("ValueOf deve retornar PREMIUM")
    void value_of_deve_retornar_premium() {
        Plano plano = Plano.valueOf("PREMIUM");
        assertEquals(Plano.PREMIUM, plano);
    }

    @Test
    @DisplayName("ValueOf deve lançar exceção para valor inválido")
    void value_of_deve_lancar_excecao_para_valor_invalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            Plano.valueOf("INVALIDO");
        });
    }

    @Test
    @DisplayName("ToString deve retornar nome do enum")
    void to_string_deve_retornar_nome_do_enum() {
        assertEquals("BASICO", Plano.BASICO.toString());
        assertEquals("PREMIUM", Plano.PREMIUM.toString());
    }

    @Test
    @DisplayName("Name deve retornar nome do enum")
    void name_deve_retornar_nome_do_enum() {
        assertEquals("BASICO", Plano.BASICO.name());
        assertEquals("PREMIUM", Plano.PREMIUM.name());
    }

    @Test
    @DisplayName("Ordinal de BASICO deve ser 0")
    void ordinal_de_basico_deve_ser_zero() {
        assertEquals(0, Plano.BASICO.ordinal());
    }

    @Test
    @DisplayName("Ordinal de PREMIUM deve ser 1")
    void ordinal_de_premium_deve_ser_um() {
        assertEquals(1, Plano.PREMIUM.ordinal());
    }

    @Test
    @DisplayName("Comparação de igualdade deve funcionar")
    void comparacao_de_igualdade_deve_funcionar() {
        Plano plano1 = Plano.BASICO;
        Plano plano2 = Plano.BASICO;

        assertEquals(plano1, plano2);
        assertSame(plano1, plano2);
    }

    @Test
    @DisplayName("Comparação de diferença deve funcionar")
    void comparacao_de_diferenca_deve_funcionar() {
        Plano plano1 = Plano.BASICO;
        Plano plano2 = Plano.PREMIUM;

        assertNotEquals(plano1, plano2);
        assertNotSame(plano1, plano2);
    }

    @Test
    @DisplayName("Switch case deve funcionar com enum")
    void switch_case_deve_funcionar_com_enum() {
        Plano plano = Plano.BASICO;

        String resultado = switch (plano) {
            case BASICO -> "É básico";
            case PREMIUM -> "É premium";
        };

        assertEquals("É básico", resultado);
    }

    @Test
    @DisplayName("GetNome não deve retornar null")
    void get_nome_nao_deve_retornar_null() {
        assertNotNull(Plano.BASICO.getNome());
        assertNotNull(Plano.PREMIUM.getNome());
    }

    @Test
    @DisplayName("HashCode deve ser consistente")
    void hash_code_deve_ser_consistente() {
        int hash1 = Plano.BASICO.hashCode();
        int hash2 = Plano.BASICO.hashCode();

        assertEquals(hash1, hash2);
    }
}
