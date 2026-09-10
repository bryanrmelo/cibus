package br.com.cibus.formasdepagamento;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public class NovaFormaDePagamentoRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    private String nome;

    public FormaDePagamento toEntity() {
        FormaDePagamento formaDePagamento = new FormaDePagamento();
        formaDePagamento.setNome(nome);
        return formaDePagamento;
    }
}
