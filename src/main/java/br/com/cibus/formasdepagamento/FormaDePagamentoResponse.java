package br.com.cibus.formasdepagamento;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormaDePagamentoResponse {
    @JsonProperty
    private Long id;

    @JsonProperty
    private String nome;

    public FormaDePagamentoResponse(FormaDePagamento formaDePagamento) {
        this(formaDePagamento.getId(), formaDePagamento.getNome());
    }

    public FormaDePagamentoResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
