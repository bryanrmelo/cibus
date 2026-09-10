package br.com.cibus.formasdepagamento;

import br.com.cibus.controller.FormaDePagamentoController;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.service.FormaDePagamentoService;
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
    private FormaDePagamentoService formaDePagamentoService;

    private final ObjectMapper jsonParser = new ObjectMapper();

    @Test
    void deveCriarNovaFormaDePagamento() throws Exception {
        String novaFormaDePagamentoJson = """
            { "nome": "PIX" }
        """;

        when(formaDePagamentoService.create(any())).thenReturn(new FormaDePagamento("PIX"));

        mockMvc.perform(post("/formas-de-pagamento").contentType(MediaType.APPLICATION_JSON).content(novaFormaDePagamentoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("PIX"));

        verify(formaDePagamentoService).create(any());
    }

    @Test
    void naoDeveCriarFormaDePagamentoComNomeInvalido() throws Exception {
        String semNomeJson = """
            { "nome": "" }
        """;

        mockMvc.perform(post("/formas-de-pagamento").contentType(MediaType.APPLICATION_JSON).content(semNomeJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(formaDePagamentoService);
    }

    @Test
    void deveListarFormasDePagamento() throws Exception {
        when(formaDePagamentoService.list()).thenReturn(List.of(
                new FormaDePagamento("Cartão de Crédito"),
                new FormaDePagamento("PIX")
        ));

        MvcResult mvcResult = mockMvc.perform(get("/formas-de-pagamento"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        List<Map<String, String>> responseData = jsonParser.readValue(responseJson, List.class);

        assertThat(responseData).hasSize(2);

        verify(formaDePagamentoService).list();
        verifyNoMoreInteractions(formaDePagamentoService);
    }

    @Test
    void deveAtualizarFormaDePagamentoExistente() throws Exception {
        FormaDePagamento existente = new FormaDePagamento("Vale Refeição/Alimentação");
        existente.setId(1L);

        when(formaDePagamentoService.update(eq(1L), any())).thenReturn(existente);

        String atualizacaoJson = """
            { "nome": "Vale Refeição/Alimentação" }
        """;

        mockMvc.perform(put("/formas-de-pagamento/1").contentType(MediaType.APPLICATION_JSON).content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vale Refeição/Alimentação"));

        verify(formaDePagamentoService).update(eq(1L), any());
    }

    @Test
    void deveRemoverFormaDePagamentoExistente() throws Exception {
        mockMvc.perform(delete("/formas-de-pagamento/4"))
                .andExpect(status().isNoContent());

        verify(formaDePagamentoService).remove(4L);
    }

    @Test
    void naoDeveAtualizarFormaDePagamentoInexistente() throws Exception {
        when(formaDePagamentoService.update(eq(404L), any()))
                .thenThrow(new EntityNotFoundException("Forma de pagamento não existe"));

        String atualizacaoJson = """
            { "nome": "Qualquer" }
        """;

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> mockMvc.perform(
                        put("/formas-de-pagamento/404").contentType(MediaType.APPLICATION_JSON).content(atualizacaoJson)))
                .hasCauseInstanceOf(EntityNotFoundException.class);
    }
}
