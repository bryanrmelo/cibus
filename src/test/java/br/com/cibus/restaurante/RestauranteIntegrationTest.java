package br.com.cibus.restaurante;

import br.com.cibus.IntegrationTestBase;
import br.com.cibus.repository.RestauranteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestauranteIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestauranteRepository restauranteRepository;

    // id 1 = "Árabe", inserido pela migration V2 (carga inicial de tipo_de_cozinha)
    private static final long TIPO_DE_COZINHA_ID_EXISTENTE = 1L;

    @Test
    void deveCriarAtualizarERemoverRestauranteDeVerdadeNoBanco() throws Exception {
        String novoRestauranteJson = """
            {
                "nome": "Restaurante Teste de Integração",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "Restaurante criado por teste automatizado",
                "tipoDeCozinha": %d
            }
        """.formatted(TIPO_DE_COZINHA_ID_EXISTENTE);

        String createResponse = mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoRestauranteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste de Integração"))
                .andReturn().getResponse().getContentAsString();

        Long id = com.jayway.jsonpath.JsonPath.parse(createResponse).read("$.id", Long.class);
        assertThat(restauranteRepository.findById(id)).isPresent();
        assertThat(restauranteRepository.findById(id).get().getTipoDeCozinha().getId()).isEqualTo(TIPO_DE_COZINHA_ID_EXISTENTE);

        String atualizacaoJson = """
            {
                "nome": "Restaurante Teste de Integração Atualizado",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "Atualizado",
                "tipoDeCozinha": %d
            }
        """.formatted(TIPO_DE_COZINHA_ID_EXISTENTE);

        mockMvc.perform(put("/restaurantes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste de Integração Atualizado"));

        assertThat(restauranteRepository.findById(id).get().getNome())
                .isEqualTo("Restaurante Teste de Integração Atualizado");

        mockMvc.perform(delete("/restaurantes/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(restauranteRepository.findById(id)).isEmpty();
    }

    @Test
    void naoDeveCriarRestauranteComTipoDeCozinhaInexistente() throws Exception {
        String jsonComTipoInvalido = """
            {
                "nome": "Restaurante Com Tipo Inválido",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "x",
                "tipoDeCozinha": 9999
            }
        """;

        assertThatThrownBy(() -> mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonComTipoInvalido)))
                .hasCauseInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    @Test
    void deveListarRestaurantesPorTipoDeCozinha() throws Exception {
        mockMvc.perform(get("/restaurantes").param("tipoDeCozinhaId", String.valueOf(TIPO_DE_COZINHA_ID_EXISTENTE)))
                .andExpect(status().isOk());
        // os restaurantes da carga inicial (V4) já garantem retorno não vazio pro tipo "Árabe"
    }
}
