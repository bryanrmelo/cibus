package br.com.cibus.dto.formasdepagamento;

import br.com.cibus.model.FormaDePagamento;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AtualizaFormaDePagamentoRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    @NotNull
    private String nome;

    public void atualiza(FormaDePagamento formaDePagamento) {
        formaDePagamento.setNome(nome);
    }
}
