package br.com.cibus.dto.formasdepagamento;

import br.com.cibus.model.FormaDePagamento;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NovaFormaDePagamentoRequest {

    @JsonProperty
    @Size(min = 1, max = 50)
    @NotNull
    private String nome;

    public FormaDePagamento toEntity() {
        FormaDePagamento formaDePagamento = new FormaDePagamento();
        formaDePagamento.setNome(nome);
        return formaDePagamento;
    }
}
