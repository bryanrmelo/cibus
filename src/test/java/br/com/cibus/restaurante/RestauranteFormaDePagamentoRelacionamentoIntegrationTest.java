package br.com.cibus.restaurante;

import br.com.cibus.IntegrationTestBase;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.repository.FormaDePagamentoRepository;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.repository.TipoDeCozinhaRepository;
import br.com.cibus.model.Restaurante;
import br.com.cibus.repository.RestauranteRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Valida a relação @ManyToMany entre Restaurante e FormaDePagamento (tabela de junção
 * restaurante_forma_pagamento), persistindo de verdade num banco real via Testcontainers
 * e recarregando os dados do zero (flush + clear do EntityManager) pra garantir que o que
 * está sendo verificado veio do banco, não só de um objeto em memória.
 */
@Transactional
class RestauranteFormaDePagamentoRelacionamentoIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;

    @Autowired
    private TipoDeCozinhaRepository tipoDeCozinhaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deveAssociarVariasFormasDePagamentoAUmRestaurante() {
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(1L).orElseThrow();

        FormaDePagamento pix = formaDePagamentoRepository.findByNome("PIX").get(0);
        FormaDePagamento cartaoCredito = formaDePagamentoRepository.findByNome("Cartão de Crédito").get(0);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Relacionamento Teste");
        restaurante.setCep("01310100");
        restaurante.setEndereco("Av. Paulista, 1000");
        restaurante.setCnpj("12345678901234");
        restaurante.setDescricao("teste de relacionamento");
        restaurante.setTipoDeCozinha(tipoDeCozinha);
        restaurante.setFormasDePagamento(new HashSet<>(Set.of(pix, cartaoCredito)));

        restauranteRepository.save(restaurante);

        // força ir ao banco de verdade na próxima leitura, em vez de reaproveitar o objeto em memória
        entityManager.flush();
        entityManager.clear();

        Restaurante restauranteRecarregado = restauranteRepository.findById(restaurante.getId()).orElseThrow();

        assertThat(restauranteRecarregado.getFormasDePagamento())
                .hasSize(2)
                .extracting(FormaDePagamento::getNome)
                .containsExactlyInAnyOrder("PIX", "Cartão de Crédito");
    }

    @Test
    void deveRemoverAssociacaoDeFormaDePagamentoSemApagarARegistroOriginal() {
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(1L).orElseThrow();
        FormaDePagamento valeRefeicao = formaDePagamentoRepository.findByNome("Vale Refeição").get(0);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Remove Associação Teste");
        restaurante.setCep("01310100");
        restaurante.setEndereco("Av. Paulista, 1000");
        restaurante.setCnpj("12345678901234");
        restaurante.setDescricao("teste");
        restaurante.setTipoDeCozinha(tipoDeCozinha);
        restaurante.setFormasDePagamento(new HashSet<>(Set.of(valeRefeicao)));
        restauranteRepository.save(restaurante);

        entityManager.flush();
        entityManager.clear();

        Restaurante restauranteRecarregado = restauranteRepository.findById(restaurante.getId()).orElseThrow();
        restauranteRecarregado.setFormasDePagamento(new HashSet<>());
        restauranteRepository.save(restauranteRecarregado);

        entityManager.flush();
        entityManager.clear();

        assertThat(restauranteRepository.findById(restaurante.getId()).orElseThrow().getFormasDePagamento()).isEmpty();
        // o registro de "Vale Refeição" continua existindo — só a associação foi removida
        assertThat(formaDePagamentoRepository.findByNome("Vale Refeição")).isNotEmpty();
    }
}
