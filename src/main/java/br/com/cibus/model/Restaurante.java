package br.com.cibus.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String cnpj;

    @Column
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "tipo_de_cozinha_id")
    private TipoDeCozinha tipoDeCozinha;

    @ManyToMany
    @JoinTable(
            name = "restaurante_forma_pagamento",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id")
    )
    private Set<FormaDePagamento> formasDePagamento = new HashSet<>();

    @Deprecated
    public Restaurante() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoDeCozinha getTipoDeCozinha() {
        return tipoDeCozinha;
    }

    public void setTipoDeCozinha(TipoDeCozinha tipoDeCozinha) {
        this.tipoDeCozinha = tipoDeCozinha;
    }
    public Set<FormaDePagamento> getFormasDePagamento() {
        return formasDePagamento;
    }
    public void setFormasDePagamento(Set<FormaDePagamento> formasDePagamento) {
        this.formasDePagamento = formasDePagamento;
    }
}
