package br.com.cibus.tipodecozinha;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public class AtualizaTipoDeCozinhaRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    private String nome;

    public void atualiza(TipoDeCozinha tipoDeCozinha) {
        tipoDeCozinha.setNome(nome);
    }
}
