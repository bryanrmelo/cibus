package br.com.cibus.service;

import br.com.cibus.dto.tipodecozinha.AtualizaTipoDeCozinhaRequest;
import br.com.cibus.dto.tipodecozinha.NovoTipoDeCozinhaRequest;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.repository.TipoDeCozinhaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDeCozinhaService {

    private final TipoDeCozinhaRepository tipoDeCozinhaRepository;

    public TipoDeCozinhaService(TipoDeCozinhaRepository tipoDeCozinhaRepository) {
        this.tipoDeCozinhaRepository = tipoDeCozinhaRepository;
    }

    public List<TipoDeCozinha> list() {
        return tipoDeCozinhaRepository.findAll();
    }

    public TipoDeCozinha create(NovoTipoDeCozinhaRequest request) {
        TipoDeCozinha novoTipoDeCozinha = request.toEntity();
        tipoDeCozinhaRepository.save(novoTipoDeCozinha);
        return novoTipoDeCozinha;
    }

    public TipoDeCozinha update(Long id, AtualizaTipoDeCozinhaRequest request) {
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        request.atualiza(tipoDeCozinha);
        tipoDeCozinhaRepository.save(tipoDeCozinha);

        return tipoDeCozinha;
    }

    public void remove(Long id) {
        if (!tipoDeCozinhaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de cozinha não encontrado");
        }

        tipoDeCozinhaRepository.deleteById(id);
    }
}
