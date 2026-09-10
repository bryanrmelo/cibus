package br.com.cibus.formasdepagamento;

import br.com.cibus.restaurante.RestauranteRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FormaDePagamentoController.class)
class FormaDePagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FormaDePagamentoRepository formaDePagamentoRepository;

    // o FormaDePagamentoController injeta o RestauranteRepository tambem, entao precisa ser mockado
    // pra o contexto do @WebMvcTest conseguir montar o controller
    @MockBean
    private RestauranteRepository restauranteRepository;

    private final ObjectMapper jsonParser = new ObjectMapper();

    @Test
    void deveCriarNovaFormaDePagamento() throws Exception {
        String novaFormaDePagamentoJson = """
            { "nome": "PIX" }
        """;

        mockMvc.perform(post("/formas-de-pagamento").contentType(MediaType.APPLICATION_JSON).content(novaFormaDePagamentoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("PIX"));

        verify(formaDePagamentoRepository).save(any(FormaDePagamento.class));
    }

    @Test
    void naoDeveCriarFormaDePagamentoComNomeInvalido() throws Exception {
        String semNomeJson = """
            { "nome": "" }
        """;

        mockMvc.perform(post("/formas-de-pagamento").contentType(MediaType.APPLICATION_JSON).content(semNomeJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(formaDePagamentoRepository);
    }

    @Test
    void deveListarFormasDePagamento() throws Exception {
        when(formaDePagamentoRepository.findAll()).thenReturn(List.of(
                new FormaDePagamento("Cartão de Crédito"),
                new FormaDePagamento("PIX")
        ));

        MvcResult mvcResult = mockMvc.perform(get("/formas-de-pagamento"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        List<Map<String, String>> responseData = jsonParser.readValue(responseJson, List.class);

        assertThat(responseData).hasSize(2);

        verify(formaDePagamentoRepository).findAll();
        verifyNoMoreInteractions(formaDePagamentoRepository);
    }

    @Test
    void deveAtualizarFormaDePagamentoExistente() throws Exception {
        FormaDePagamento existente = new FormaDePagamento("Vale Refeição");
        existente.setId(1L);

        when(formaDePagamentoRepository.findById(1L)).thenReturn(Optional.of(existente));

        String atualizacaoJson = """
            { "nome": "Vale Refeição/Alimentação" }
        """;

        mockMvc.perform(put("/formas-de-pagamento/1").contentType(MediaType.APPLICATION_JSON).content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vale Refeição/Alimentação"));

        verify(formaDePagamentoRepository).save(existente);
    }

    @Test
    void deveRemoverFormaDePagamentoExistente() throws Exception {
        FormaDePagamento existente = new FormaDePagamento("PIX");
        existente.setId(4L);

        when(formaDePagamentoRepository.findById(4L)).thenReturn(Optional.of(existente));

        mockMvc.perform(delete("/formas-de-pagamento/4"))
                .andExpect(status().isNoContent());

        verify(formaDePagamentoRepository).deleteById(4L);
    }
}
