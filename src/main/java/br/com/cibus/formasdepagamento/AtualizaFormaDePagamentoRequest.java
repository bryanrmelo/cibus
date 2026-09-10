package br.com.cibus.formasdepagamento;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public class AtualizaFormaDePagamentoRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    private String nome;

    public void atualiza(FormaDePagamento formaDePagamento) {
        formaDePagamento.setNome(nome);
    }
}
