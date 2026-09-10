package br.com.cibus.restaurante;

import br.com.cibus.tipodecozinha.TipoDeCozinha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByTipoDeCozinhaId(Long tipoDeCozinhaId);
}
