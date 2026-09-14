package br.com.cibus.dto.restaurante.projection;

/**
 * Projection (interface-based projection do Spring Data).
 *
 * Nao existe implementacao dessa interface no projeto: o Spring Data cria um proxy
 * em tempo de execucao e liga cada getter a uma coluna do resultado da query pelo
 * nome do alias -> "nome" alimenta getNome(), "quantidade" alimenta getQuantidade().
 * Por isso os alias da query precisam bater com os nomes das propriedades.
 *
 * Vantagem: o banco devolve so as duas colunas do relatorio, sem carregar a entidade
 * Restaurante inteira nem precisar de uma classe DTO com construtor.
 */
public interface RelatorioTipoDeCozinha {

    String getNome();

    long getQuantidade();

    /**
     * Metodo default: nao vem do banco, e calculado em cima do que o proxy devolveu.
     * Formato final do relatorio -> "Italiana: 3"
     */
    default String getResumo() {
        return getNome() + ": " + getQuantidade();
    }
}
