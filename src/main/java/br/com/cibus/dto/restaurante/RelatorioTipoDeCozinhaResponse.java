package br.com.cibus.dto.restaurante;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RelatorioTipoDeCozinhaResponse {

    @JsonProperty
    private String nome;

    @JsonProperty
    private int quantidade;

    public RelatorioTipoDeCozinhaResponse(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }
}
