package br.com.cibus.restaurante;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;

    public RestauranteController(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @GetMapping("/restaurante-por-tipo-de-cozinha")
    public List<RestauranteResponse> listByTipo(@RequestParam Long tipoDeCozinhaId) {
        return restauranteRepository.findByTipoDeCozinhaId(tipoDeCozinhaId)
                .stream()
                .map(RestauranteResponse::new)
                .toList();
    }

    @GetMapping("/count-restaurante-por-tipo-de-cozinha")
    public int countByTipo(@RequestParam Long tipoDeCozinhaId) {
        return restauranteRepository.countByTipoDeCozinhaId(tipoDeCozinhaId);
    }
}
