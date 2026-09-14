package br.com.cibus.repository;

import br.com.cibus.dto.restaurante.projection.RelatorioTipoDeCozinha;
import br.com.cibus.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByTipoDeCozinhaId(Long tipoDeCozinhaId);
    List<Restaurante> findByFormasDePagamentoNome(String formaDePagamentoNome);
    int countByTipoDeCozinhaId(Long tipoDeCozinhaId);

    // os alias "nome" e "quantidade" sao o que liga o resultado aos getters da projection
    @Query(nativeQuery = true, value = """
            select tc.nome as nome, count(r.id) as quantidade
            from tipo_de_cozinha tc
            left join restaurante r on r.tipo_de_cozinha_id = tc.id
            group by tc.id, tc.nome
            order by quantidade desc, tc.nome
            """)
    List<RelatorioTipoDeCozinha> gerarRelatorioPorTipoDeCozinha();
}
