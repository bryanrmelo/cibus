package br.com.cibus.model.relatorios;

public class RelatorioTipoDeCozinha {

    private String nome;

    private int quantidade;

    public RelatorioTipoDeCozinha(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
