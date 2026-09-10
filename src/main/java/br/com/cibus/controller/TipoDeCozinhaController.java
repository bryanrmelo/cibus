package br.com.cibus.controller;

import br.com.cibus.dto.tipodecozinha.AtualizaTipoDeCozinhaRequest;
import br.com.cibus.dto.tipodecozinha.NovoTipoDeCozinhaRequest;
import br.com.cibus.dto.tipodecozinha.TipoDeCozinhaResponse;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.service.TipoDeCozinhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TipoDeCozinhaController {

    private final TipoDeCozinhaService tipoDeCozinhaService;

    public TipoDeCozinhaController(TipoDeCozinhaService tipoDeCozinhaService) {
        this.tipoDeCozinhaService = tipoDeCozinhaService;
    }

    @GetMapping("/tipos-de-cozinha")
    public List<TipoDeCozinhaResponse> list() {
        return tipoDeCozinhaService.list().stream().map(TipoDeCozinhaResponse::new).toList();
    }

    @PostMapping("/tipos-de-cozinha")
    public ResponseEntity<TipoDeCozinhaResponse> create(@RequestBody @Valid NovoTipoDeCozinhaRequest novoTipoDeCozinhaRequest) {
        TipoDeCozinha novoTipoDeCozinha = tipoDeCozinhaService.create(novoTipoDeCozinhaRequest);
        return new ResponseEntity<>(new TipoDeCozinhaResponse(novoTipoDeCozinha), HttpStatus.CREATED);
    }

    @PutMapping("/tipos-de-cozinha/{id}")
    public ResponseEntity<TipoDeCozinhaResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaTipoDeCozinhaRequest request) {
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaService.update(id, request);
        return ResponseEntity.ok(new TipoDeCozinhaResponse(tipoDeCozinha));
    }

    @DeleteMapping("/tipos-de-cozinha/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        tipoDeCozinhaService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
