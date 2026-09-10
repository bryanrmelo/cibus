package br.com.cibus.restaurante;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestauranteResponse {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String nome;

    public RestauranteResponse(Restaurante restaurante) {
        this(restaurante.getId(), restaurante.getNome());
    }

    public RestauranteResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
