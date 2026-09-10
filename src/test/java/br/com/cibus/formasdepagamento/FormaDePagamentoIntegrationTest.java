package br.com.cibus.formasdepagamento;

import br.com.cibus.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FormaDePagamentoIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;

    @Test
    void devePersistirEListarFormasDePagamentoDaCargaInicial() throws Exception {
        // as 4 formas de pagamento iniciais vêm da migration V6 (Flyway roda antes dos testes)
        mockMvc.perform(get("/formas-de-pagamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[*].nome").value(org.hamcrest.Matchers.hasItems(
                        "Cartão de Crédito", "Cartão de Débito", "Vale Refeição", "PIX")));
    }

    @Test
    void deveCriarAtualizarERemoverFormaDePagamentoDeVerdadeNoBanco() throws Exception {
        String novaFormaDePagamentoJson = """
            { "nome": "Vale Alimentação" }
        """;

        String createResponse = mockMvc.perform(post("/formas-de-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaFormaDePagamentoJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = com.jayway.jsonpath.JsonPath.parse(createResponse).read("$.id", Long.class);
        assertThat(formaDePagamentoRepository.findById(id)).isPresent();

        String atualizacaoJson = """
            { "nome": "Vale Alimentação/Refeição" }
        """;

        mockMvc.perform(put("/formas-de-pagamento/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vale Alimentação/Refeição"));

        assertThat(formaDePagamentoRepository.findById(id).get().getNome()).isEqualTo("Vale Alimentação/Refeição");

        mockMvc.perform(delete("/formas-de-pagamento/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(formaDePagamentoRepository.findById(id)).isEmpty();
    }

    @Test
    void naoDeveCriarFormaDePagamentoComNomeDuplicado() throws Exception {
        String jsonComNomeJaExistente = """
            { "nome": "PIX" }
        """;

        assertThatThrownBy(() -> mockMvc.perform(post("/formas-de-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonComNomeJaExistente)))
                .hasCauseInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}
