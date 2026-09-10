package br.com.cibus.dto.restaurante;

import br.com.cibus.dto.formasdepagamento.FormaDePagamentoResponse;
import br.com.cibus.dto.tipodecozinha.TipoDeCozinhaResponse;
import br.com.cibus.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RestauranteResponse {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String nome;

    @JsonProperty
    private String cep;

    @JsonProperty
    private String endereco;

    @JsonProperty
    private String cnpj;

    @JsonProperty
    private String descricao;

    @JsonProperty
    private TipoDeCozinhaResponse tipoDeCozinha;

    @JsonProperty
    private List<FormaDePagamentoResponse> formasDePagamento;

    public RestauranteResponse(Restaurante restaurante) {
        this.id = restaurante.getId();
        this.nome = restaurante.getNome();
        this.cep = restaurante.getCep();
        this.endereco = restaurante.getEndereco();
        this.cnpj = restaurante.getCnpj();
        this.descricao = restaurante.getDescricao();
        this.tipoDeCozinha = new TipoDeCozinhaResponse(restaurante.getTipoDeCozinha());
        this.formasDePagamento = restaurante.getFormasDePagamento()
                .stream()
                .map(FormaDePagamentoResponse::new)
                .toList();
    }
}
