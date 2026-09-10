package br.com.cibus.dto.restaurante;

import br.com.cibus.model.Restaurante;
import br.com.cibus.model.TipoDeCozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NovoRestauranteRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    @NotNull
    private String nome;

    @JsonProperty
    @Size(min = 1, max = 8)
    @NotNull
    private String cep;

    @JsonProperty
    @Size(min = 1, max = 200)
    @NotNull
    private String endereco;

    @JsonProperty
    @Size(min = 14, max = 14)
    @NotNull
    private String cnpj;

    @JsonProperty
    private String descricao;

    @JsonProperty
    @NotNull
    private Long tipoDeCozinha;

    public Long getTipoDeCozinha() {
        return tipoDeCozinha;
    }

    public Restaurante toEntity(TipoDeCozinha tipoDeCozinha) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(nome);
        restaurante.setCep(cep);
        restaurante.setEndereco(endereco);
        restaurante.setCnpj(cnpj);
        restaurante.setDescricao(descricao);
        restaurante.setTipoDeCozinha(tipoDeCozinha);
        return restaurante;
    }
}
