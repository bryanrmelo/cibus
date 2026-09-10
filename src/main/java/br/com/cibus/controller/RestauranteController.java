package br.com.cibus.controller;

import br.com.cibus.dto.restaurante.AtualizaRestauranteRequest;
import br.com.cibus.dto.restaurante.NovoRestauranteRequest;
import br.com.cibus.dto.restaurante.RestauranteResponse;
import br.com.cibus.model.Restaurante;
import br.com.cibus.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> getOne(@PathVariable Long id) {
        Restaurante restaurante = restauranteService.getOne(id);
        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }

    @GetMapping("/restaurantes")
    public List<RestauranteResponse> list(
            @RequestParam(required = false) Long tipoDeCozinhaId,
            @RequestParam(required = false) String formaDePagamento) {

        return restauranteService.list(tipoDeCozinhaId, formaDePagamento)
                .stream().map(RestauranteResponse::new).toList();
    }

    @GetMapping("/restaurantes/count")
    public int countByTipo(@RequestParam Long tipoDeCozinhaId) {
        return restauranteService.countByTipo(tipoDeCozinhaId);
    }

    @PostMapping("/restaurantes")
    public ResponseEntity<RestauranteResponse> create(@RequestBody @Valid NovoRestauranteRequest request) {
        Restaurante novoRestaurante = restauranteService.create(request);
        return new ResponseEntity<>(new RestauranteResponse(novoRestaurante), HttpStatus.CREATED);
    }

    @PutMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaRestauranteRequest request) {
        Restaurante restaurante = restauranteService.update(id, request);
        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }

    @DeleteMapping("/restaurantes/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        restauranteService.remove(id);
        return ResponseEntity.noContent().build();
    }

    // associa uma forma de pagamento a um restaurante
    @PostMapping("/restaurantes/{restauranteId}/forma-de-pagamento/{formaDePagamentoId}")
    public ResponseEntity<RestauranteResponse> associarFormaDePagamento(
            @PathVariable Long restauranteId,
            @PathVariable Long formaDePagamentoId) {

        Restaurante restaurante = restauranteService.associarFormaDePagamento(restauranteId, formaDePagamentoId);
        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }

    // dessasocia uma forma de pagamento de um restaurante
    @DeleteMapping("/restaurantes/{restauranteId}/forma-de-pagamento/{formaDePagamentoId}")
    public ResponseEntity<RestauranteResponse> desassociarFormaDePagamento(
            @PathVariable Long restauranteId,
            @PathVariable Long formaDePagamentoId) {

        Restaurante restaurante = restauranteService.desassociarFormaDePagamento(restauranteId, formaDePagamentoId);
        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }
}
