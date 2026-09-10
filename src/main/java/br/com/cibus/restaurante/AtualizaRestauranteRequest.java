package br.com.cibus.restaurante;

import br.com.cibus.tipodecozinha.TipoDeCozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AtualizaRestauranteRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    private String nome;

    @JsonProperty
    @Size(min = 1, max = 8)
    private String cep;

    @JsonProperty
    @Size(min = 1, max = 200)
    private String endereco;

    @JsonProperty
    @Size(min = 14, max = 14)
    private String cnpj;

    @JsonProperty
    private String descricao;

    @JsonProperty
    @NotNull
    private Long tipoDeCozinha;

    public Long getTipoDeCozinha() {
        return tipoDeCozinha;
    }

    public void atualiza(Restaurante restaurante, TipoDeCozinha tipoDeCozinha) {
        restaurante.setNome(nome);
        restaurante.setCep(cep);
        restaurante.setEndereco(endereco);
        restaurante.setCnpj(cnpj);
        restaurante.setDescricao(descricao);
        restaurante.setTipoDeCozinha(tipoDeCozinha);
    }
}
