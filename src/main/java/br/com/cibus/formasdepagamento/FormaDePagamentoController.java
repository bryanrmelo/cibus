package br.com.cibus.formasdepagamento;

import br.com.cibus.restaurante.*;
import br.com.cibus.tipodecozinha.TipoDeCozinha;
import br.com.cibus.tipodecozinha.TipoDeCozinhaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FormaDePagamentoController {

    private final FormaDePagamentoRepository formaDePagamentoRepository;
    private final RestauranteRepository restauranteRepository;

    public FormaDePagamentoController(FormaDePagamentoRepository formaDePagamentoRepository,RestauranteRepository restauranteRepository) {
        this.formaDePagamentoRepository = formaDePagamentoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    @GetMapping("/formas-de-pagamento")
    public List<FormaDePagamentoResponse> list() {
        return formaDePagamentoRepository.findAll()
                .stream()
                .map(FormaDePagamentoResponse::new)
                .toList();
    }

    @PostMapping("/formas-de-pagamento")
    public ResponseEntity<FormaDePagamentoResponse> create(@RequestBody @Valid NovaFormaDePagamentoRequest request) {
        // instancia um restaurante se baseando no DTO
        FormaDePagamento novaFormaDePagamento = request.toEntity();

        // salva
        formaDePagamentoRepository.save(novaFormaDePagamento);

        // retorna uma ResponseEntitity com tipo Restaurante e codigo HTTP CREATED
        return new ResponseEntity<>(new FormaDePagamentoResponse(novaFormaDePagamento), HttpStatus.CREATED);
    }

    @PutMapping("/formas-de-pagamento/{id}")
    public ResponseEntity<FormaDePagamentoResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaFormaDePagamentoRequest request) {
        // busca os dados já existentes
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));

        // atualiza os dados e salva no banco atualizados
        request.atualiza(formaDePagamento);
        formaDePagamentoRepository.save(formaDePagamento);

        // retorna com 200
        return ResponseEntity.ok(new FormaDePagamentoResponse(formaDePagamento));
    }

    @DeleteMapping("/formas-de-pagamento/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        // busca os dados do restaurante já existentes
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));
        formaDePagamentoRepository.deleteById(formaDePagamento.getId());
        return ResponseEntity.noContent().build();
    }
}
