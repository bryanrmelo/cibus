package br.com.cibus.restaurante;

import br.com.cibus.tipodecozinha.TipoDeCozinha;
import br.com.cibus.tipodecozinha.TipoDeCozinhaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestauranteController.class)
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestauranteRepository restauranteRepository;

    @MockBean
    private TipoDeCozinhaRepository tipoDeCozinhaRepository;

    private final ObjectMapper jsonParser = new ObjectMapper();

    private TipoDeCozinha tipoDeCozinhaValido() {
        TipoDeCozinha tipoDeCozinha = new TipoDeCozinha("Italiana");
        tipoDeCozinha.setId(1L);
        return tipoDeCozinha;
    }

    @Test
    void deveCriarNovoRestaurante() throws Exception {
        when(tipoDeCozinhaRepository.findById(1L)).thenReturn(Optional.of(tipoDeCozinhaValido()));

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

        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void naoDeveCriarRestauranteComTipoDeCozinhaInexistente() throws Exception {
        when(tipoDeCozinhaRepository.findById(99L)).thenReturn(Optional.empty());

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

        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void deveListarRestaurantesPorTipoDeCozinha() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Cantina da Nonna");

        when(restauranteRepository.findByTipoDeCozinhaId(1L)).thenReturn(List.of(restaurante));

        MvcResult mvcResult = mockMvc.perform(get("/restaurantes").param("tipoDeCozinhaId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        List<Map<String, String>> responseData = jsonParser.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(responseData).hasSize(1);

        verify(restauranteRepository).findByTipoDeCozinhaId(1L);
    }

    @Test
    void deveContarRestaurantesPorTipoDeCozinha() throws Exception {
        when(restauranteRepository.countByTipoDeCozinhaId(1L)).thenReturn(3);

        mockMvc.perform(get("/restaurantes/count").param("tipoDeCozinhaId", "1"))
                .andExpect(status().isOk());

        verify(restauranteRepository).countByTipoDeCozinhaId(1L);
    }

    @Test
    void deveAtualizarRestauranteExistente() throws Exception {
        Restaurante existente = new Restaurante();
        existente.setId(5L);
        existente.setNome("Cantina da Nonna");

        when(restauranteRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(tipoDeCozinhaRepository.findById(1L)).thenReturn(Optional.of(tipoDeCozinhaValido()));

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

        verify(restauranteRepository).save(existente);
    }

    @Test
    void naoDeveAtualizarRestauranteInexistente() throws Exception {
        when(restauranteRepository.findById(404L)).thenReturn(Optional.empty());

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

        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void deveRemoverRestauranteExistente() throws Exception {
        Restaurante existente = new Restaurante();
        existente.setId(5L);

        when(restauranteRepository.findById(5L)).thenReturn(Optional.of(existente));

        mockMvc.perform(delete("/restaurantes/5"))
                .andExpect(status().isNoContent());

        verify(restauranteRepository).deleteById(5L);
    }
}
