package br.com.cibus.restaurante;

import br.com.cibus.formasdepagamento.FormaDePagamento;
import br.com.cibus.formasdepagamento.FormaDePagamentoRepository;
import br.com.cibus.tipodecozinha.TipoDeCozinha;
import br.com.cibus.tipodecozinha.TipoDeCozinhaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final TipoDeCozinhaRepository tipoDeCozinhaRepository;
    private final FormaDePagamentoRepository formaDePagamentoRepository;

    public RestauranteController(RestauranteRepository restauranteRepository, TipoDeCozinhaRepository tipoDeCozinhaRepository, FormaDePagamentoRepository formaDePagamentoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.tipoDeCozinhaRepository = tipoDeCozinhaRepository;
        this.formaDePagamentoRepository = formaDePagamentoRepository;
    }

    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> getOne(@PathVariable Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));

        return ResponseEntity.ok(new RestauranteResponse(restaurante));
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

    // associa uma forma de pagamento a um restaurante
    @PostMapping("/restaurantes/{restauranteId}/forma-de-pagamento/{formaDePagamentoId}")
    public ResponseEntity<RestauranteResponse> associarFormaDePagamento(
            @PathVariable Long restauranteId,
            @PathVariable Long formaDePagamentoId) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(formaDePagamentoId).orElseThrow(() -> new EntityNotFoundException(("Forma de pagamento não existe")));

        restaurante.getFormasDePagamento().add(formaDePagamento);
        restauranteRepository.save(restaurante);

        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }

    // dessasocia uma forma de pagamento de um restaurante
    @DeleteMapping("/restaurantes/{restauranteId}/forma-de-pagamento/{formaDePagamentoId}")
    public ResponseEntity<RestauranteResponse> desassociarFormaDePagamento(
            @PathVariable Long restauranteId,
            @PathVariable Long formaDePagamentoId) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(formaDePagamentoId).orElseThrow(() -> new EntityNotFoundException(("Forma de pagamento não existe")));

        restaurante.getFormasDePagamento().remove(formaDePagamento);
        restauranteRepository.save(restaurante);

        return ResponseEntity.ok(new RestauranteResponse(restaurante));
    }
}
