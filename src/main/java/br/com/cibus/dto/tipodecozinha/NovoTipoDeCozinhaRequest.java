package br.com.cibus.dto.tipodecozinha;

import br.com.cibus.model.TipoDeCozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NovoTipoDeCozinhaRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    @NotNull
    private String nome;

    public TipoDeCozinha toEntity() {
        return new TipoDeCozinha(nome);
    }

}
