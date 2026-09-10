package br.com.cibus.restaurante;

import br.com.cibus.tipodecozinha.TipoDeCozinha;
import br.com.cibus.tipodecozinha.TipoDeCozinhaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final TipoDeCozinhaRepository tipoDeCozinhaRepository;

    public RestauranteController(RestauranteRepository restauranteRepository, TipoDeCozinhaRepository tipoDeCozinhaRepository) {
        this.restauranteRepository = restauranteRepository;
        this.tipoDeCozinhaRepository = tipoDeCozinhaRepository;
    }

    @GetMapping("/restaurantes")
    public List<RestauranteResponse> list(
            @RequestParam(required = false) Long tipoDeCozinhaId,
            @RequestParam(required = false) String formaDePagamento) {

        List<Restaurante> restaurantes;

        if (tipoDeCozinhaId != null) {
            restaurantes = restauranteRepository.findByTipoDeCozinhaId(tipoDeCozinhaId);
        } else if (formaDePagamento != null) {
            restaurantes = restauranteRepository.findByFormasDePagamentoNome(formaDePagamento);
        } else {
            restaurantes = restauranteRepository.findAll();
        }

        return restaurantes.stream().map(RestauranteResponse::new).toList();
    }

    @GetMapping("/restaurantes/count")
    public int countByTipo(@RequestParam Long tipoDeCozinhaId) {
        return restauranteRepository.countByTipoDeCozinhaId(tipoDeCozinhaId);
    }

    @PostMapping("/restaurantes")
    public ResponseEntity<RestauranteResponse> create(@RequestBody @Valid NovoRestauranteRequest request) {
        // busca o objeto tipo de cozinha
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(request.getTipoDeCozinha()).orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        // instancia um restaurante se baseando no DTO
        Restaurante novoRestaurante = request.toEntity(tipoDeCozinha);

        // salva
        restauranteRepository.save(novoRestaurante);

        // retorna uma ResponseEntitity com tipo Restaurante e codigo HTTP CREATED
        return new ResponseEntity<>(new RestauranteResponse(novoRestaurante), HttpStatus.CREATED);
    }

    @PutMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaRestauranteRequest request) {
        // busca os dados do restaurante já existentes
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));

        // busca o objeto tipo de cozinha e valida
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(request.getTipoDeCozinha()).orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        // atualiza os dados e salva no banco atualizados
        request.atualiza(restaurante, tipoDeCozinha);
        restauranteRepository.save(restaurante);

        // retorna com 200
        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }

    @DeleteMapping("/restaurantes/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        // busca os dados do restaurante já existentes
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));

        restauranteRepository.deleteById(restaurante.getId());

        return ResponseEntity.noContent().build();
    }
}
