package br.com.cibus.controller;

import br.com.cibus.dto.formasdepagamento.AtualizaFormaDePagamentoRequest;
import br.com.cibus.dto.formasdepagamento.FormaDePagamentoResponse;
import br.com.cibus.dto.formasdepagamento.NovaFormaDePagamentoRequest;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.service.FormaDePagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FormaDePagamentoController {

    private final FormaDePagamentoService formaDePagamentoService;

    public FormaDePagamentoController(FormaDePagamentoService formaDePagamentoService) {
        this.formaDePagamentoService = formaDePagamentoService;
    }

    @GetMapping("/formas-de-pagamento")
    public List<FormaDePagamentoResponse> list() {
        return formaDePagamentoService.list()
                .stream()
                .map(FormaDePagamentoResponse::new)
                .toList();
    }

    @PostMapping("/formas-de-pagamento")
    public ResponseEntity<FormaDePagamentoResponse> create(@RequestBody @Valid NovaFormaDePagamentoRequest request) {
        FormaDePagamento novaFormaDePagamento = formaDePagamentoService.create(request);
        return new ResponseEntity<>(new FormaDePagamentoResponse(novaFormaDePagamento), HttpStatus.CREATED);
    }

    @PutMapping("/formas-de-pagamento/{id}")
    public ResponseEntity<FormaDePagamentoResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaFormaDePagamentoRequest request) {
        FormaDePagamento formaDePagamento = formaDePagamentoService.update(id, request);
        return ResponseEntity.ok(new FormaDePagamentoResponse(formaDePagamento));
    }

    @DeleteMapping("/formas-de-pagamento/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        formaDePagamentoService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
