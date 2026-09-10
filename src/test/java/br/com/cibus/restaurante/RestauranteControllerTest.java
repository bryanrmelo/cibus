package br.com.cibus.restaurante;

import br.com.cibus.controller.RestauranteController;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.model.Restaurante;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestauranteController.class)
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestauranteService restauranteService;

    private final ObjectMapper jsonParser = new ObjectMapper();

    private TipoDeCozinha tipoDeCozinhaValido() {
        TipoDeCozinha tipoDeCozinha = new TipoDeCozinha("Italiana");
        tipoDeCozinha.setId(1L);
        return tipoDeCozinha;
    }

    @Test
    void deveCriarNovoRestaurante() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Cantina da Nonna");
        restaurante.setTipoDeCozinha(tipoDeCozinhaValido());

        when(restauranteService.create(any())).thenReturn(restaurante);

        String novoRestauranteJson = """
            {
                "nome": "Cantina da Nonna",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "Comida italiana",
                "tipoDeCozinha": 1
            }
        """;

        mockMvc.perform(post("/restaurantes").contentType(MediaType.APPLICATION_JSON).content(novoRestauranteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Cantina da Nonna"));

        verify(restauranteService).create(any());
    }

    @Test
    void naoDeveCriarRestauranteComTipoDeCozinhaInexistente() throws Exception {
        when(restauranteService.create(any())).thenThrow(new EntityNotFoundException("Tipo de cozinha não encontrado"));

        String novoRestauranteJson = """
            {
                "nome": "Cantina da Nonna",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "Comida italiana",
                "tipoDeCozinha": 99
            }
        """;

        assertThatThrownBy(() -> mockMvc.perform(post("/restaurantes").contentType(MediaType.APPLICATION_JSON).content(novoRestauranteJson)))
                .hasCauseInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    @Test
    void deveListarRestaurantesPorTipoDeCozinha() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Cantina da Nonna");
        restaurante.setTipoDeCozinha(tipoDeCozinhaValido());

        when(restauranteService.list(1L, null)).thenReturn(List.of(restaurante));

        MvcResult mvcResult = mockMvc.perform(get("/restaurantes").param("tipoDeCozinhaId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        List<Map<String, String>> responseData = jsonParser.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(responseData).hasSize(1);

        verify(restauranteService).list(1L, null);
    }

    @Test
    void deveContarRestaurantesPorTipoDeCozinha() throws Exception {
        when(restauranteService.countByTipo(1L)).thenReturn(3);

        mockMvc.perform(get("/restaurantes/count").param("tipoDeCozinhaId", "1"))
                .andExpect(status().isOk());

        verify(restauranteService).countByTipo(1L);
    }

    @Test
    void deveAtualizarRestauranteExistente() throws Exception {
        Restaurante existente = new Restaurante();
        existente.setId(5L);
        existente.setNome("Cantina da Nonna 2");
        existente.setTipoDeCozinha(tipoDeCozinhaValido());

        when(restauranteService.update(eq(5L), any())).thenReturn(existente);

        String atualizacaoJson = """
            {
                "nome": "Cantina da Nonna 2",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "Comida italiana",
                "tipoDeCozinha": 1
            }
        """;

        mockMvc.perform(put("/restaurantes/5").contentType(MediaType.APPLICATION_JSON).content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cantina da Nonna 2"));

        verify(restauranteService).update(eq(5L), any());
    }

    @Test
    void naoDeveAtualizarRestauranteInexistente() throws Exception {
        when(restauranteService.update(eq(404L), any())).thenThrow(new EntityNotFoundException("Restaurante não existe"));

        String atualizacaoJson = """
            {
                "nome": "Qualquer",
                "cep": "01310100",
                "endereco": "Av. Paulista, 1000",
                "cnpj": "12345678901234",
                "descricao": "x",
                "tipoDeCozinha": 1
            }
        """;

        assertThatThrownBy(() -> mockMvc.perform(put("/restaurantes/404").contentType(MediaType.APPLICATION_JSON).content(atualizacaoJson)))
                .hasCauseInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    @Test
    void deveRemoverRestauranteExistente() throws Exception {
        mockMvc.perform(delete("/restaurantes/5"))
                .andExpect(status().isNoContent());

        verify(restauranteService).remove(5L);
    }

    @Test
    void deveBuscarRestaurantePorId() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Cantina da Nonna");
        restaurante.setTipoDeCozinha(tipoDeCozinhaValido());

        when(restauranteService.getOne(5L)).thenReturn(restaurante);

        mockMvc.perform(get("/restaurantes/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cantina da Nonna"));
    }

    @Test
    void naoDeveBuscarRestauranteInexistentePorId() throws Exception {
        when(restauranteService.getOne(404L)).thenThrow(new EntityNotFoundException("Restaurante não existe"));

        assertThatThrownBy(() -> mockMvc.perform(get("/restaurantes/404")))
                .hasCauseInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    @Test
    void deveAssociarFormaDePagamentoAoRestaurante() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Cantina da Nonna");
        restaurante.setTipoDeCozinha(tipoDeCozinhaValido());

        FormaDePagamento pix = new FormaDePagamento("PIX");
        pix.setId(2L);
        restaurante.getFormasDePagamento().add(pix);

        when(restauranteService.associarFormaDePagamento(5L, 2L)).thenReturn(restaurante);

        mockMvc.perform(post("/restaurantes/5/forma-de-pagamento/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formasDePagamento[0].nome").value("PIX"));

        verify(restauranteService).associarFormaDePagamento(5L, 2L);
    }

    @Test
    void deveDesassociarFormaDePagamentoDoRestaurante() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Cantina da Nonna");
        restaurante.setTipoDeCozinha(tipoDeCozinhaValido());

        when(restauranteService.desassociarFormaDePagamento(5L, 2L)).thenReturn(restaurante);

        mockMvc.perform(delete("/restaurantes/5/forma-de-pagamento/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formasDePagamento.length()").value(0));

        verify(restauranteService).desassociarFormaDePagamento(5L, 2L);
    }
}
