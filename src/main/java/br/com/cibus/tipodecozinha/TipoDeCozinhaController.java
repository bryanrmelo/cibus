package br.com.cibus.tipodecozinha;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TipoDeCozinhaController {

    private final TipoDeCozinhaRepository tipoDeCozinhaRepository;

    public TipoDeCozinhaController(TipoDeCozinhaRepository tipoDeCozinhaRepository) {
        this.tipoDeCozinhaRepository = tipoDeCozinhaRepository;
    }

    @GetMapping("/tipos-de-cozinha")
    public List<TipoDeCozinhaResponse> list() {
        return tipoDeCozinhaRepository.findAll().stream().map(TipoDeCozinhaResponse::new).toList();
    }

    @PostMapping("/tipos-de-cozinha")
    public ResponseEntity<TipoDeCozinhaResponse> create(@RequestBody @Valid NovoTipoDeCozinhaRequest novoTipoDeCozinhaRequest) {
        TipoDeCozinha novoTipoDeCozinha = novoTipoDeCozinhaRequest.toEntity();
        tipoDeCozinhaRepository.save(novoTipoDeCozinha);
        return new ResponseEntity<>(new TipoDeCozinhaResponse(novoTipoDeCozinha), HttpStatus.CREATED);
    }

    @PutMapping("/tipos-de-cozinha/{id}")
    public ResponseEntity<TipoDeCozinhaResponse> update(@PathVariable Long id, @RequestBody @Valid AtualizaTipoDeCozinhaRequest request) {
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        request.atualiza(tipoDeCozinha);
        tipoDeCozinhaRepository.save(tipoDeCozinha);

        return ResponseEntity.ok(new TipoDeCozinhaResponse(tipoDeCozinha));
    }

    @DeleteMapping("/tipos-de-cozinha/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        if (!tipoDeCozinhaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de cozinha não encontrado");
        }

        tipoDeCozinhaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
