package br.com.cibus.repository;

import br.com.cibus.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByTipoDeCozinhaId(Long tipoDeCozinhaId);
    List<Restaurante> findByFormasDePagamentoNome(String formaDePagamentoNome);
    int countByTipoDeCozinhaId(Long tipoDeCozinhaId);
}
