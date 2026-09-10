package br.com.cibus;

import br.com.cibus.controller.TipoDeCozinhaController;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.service.TipoDeCozinhaService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TipoDeCozinhaController.class)
public class TipoDeCozinhaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoDeCozinhaService tipoDeCozinhaService;

    private ObjectMapper jsonParser = new ObjectMapper();

    @Test
    void deveCriarNovoTipoDeCozinha() throws Exception {

        String novoTipoDeCozinhaJson = """
            { "nome": "Fusion" }
        """;

        when(tipoDeCozinhaService.create(any())).thenReturn(new TipoDeCozinha(1L, "Fusion"));

        mockMvc.perform(post("/tipos-de-cozinha").contentType(MediaType.APPLICATION_JSON).content(novoTipoDeCozinhaJson))
                .andExpect(status().isCreated())
                .andReturn();

        verify(tipoDeCozinhaService).create(any());

    }

    @Test
    void deveListarTiposDeCozinha() throws Exception {
        when(tipoDeCozinhaService.list()).thenReturn(List.of(new TipoDeCozinha(1L, "Árabe"),new TipoDeCozinha(2L, "Peruana") ));

        MvcResult mvcResult = mockMvc.perform(get("/tipos-de-cozinha"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        List<Map<String, String>> responseData = jsonParser.readValue(responseJson, List.class);

        assertThat(responseData).hasSize(2);

        verify(tipoDeCozinhaService).list();
        verifyNoMoreInteractions(tipoDeCozinhaService);
    }

}
