package br.com.cibus.service;

import br.com.cibus.dto.restaurante.AtualizaRestauranteRequest;
import br.com.cibus.dto.restaurante.NovoRestauranteRequest;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.model.Restaurante;
import br.com.cibus.model.TipoDeCozinha;
import br.com.cibus.model.relatorios.RelatorioTipoDeCozinha;
import br.com.cibus.repository.FormaDePagamentoRepository;
import br.com.cibus.repository.RestauranteRepository;
import br.com.cibus.repository.TipoDeCozinhaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final TipoDeCozinhaRepository tipoDeCozinhaRepository;
    private final FormaDePagamentoRepository formaDePagamentoRepository;

    public RestauranteService(RestauranteRepository restauranteRepository, TipoDeCozinhaRepository tipoDeCozinhaRepository, FormaDePagamentoRepository formaDePagamentoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.tipoDeCozinhaRepository = tipoDeCozinhaRepository;
        this.formaDePagamentoRepository = formaDePagamentoRepository;
    }

    public Restaurante getOne(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));
    }

    public List<Restaurante> list(Long tipoDeCozinhaId, String formaDePagamento) {
        if (tipoDeCozinhaId != null) {
            return restauranteRepository.findByTipoDeCozinhaId(tipoDeCozinhaId);
        } else if (formaDePagamento != null) {
            return restauranteRepository.findByFormasDePagamentoNome(formaDePagamento);
        } else {
            return restauranteRepository.findAll();
        }
    }

    public int countByTipo(Long tipoDeCozinhaId) {
        return restauranteRepository.countByTipoDeCozinhaId(tipoDeCozinhaId);
    }

    public Restaurante create(NovoRestauranteRequest request) {
        // busca o objeto tipo de cozinha
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(request.getTipoDeCozinha())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        // instancia um restaurante se baseando no DTO
        Restaurante novoRestaurante = request.toEntity(tipoDeCozinha);

        // salva
        restauranteRepository.save(novoRestaurante);

        return novoRestaurante;
    }

    public Restaurante update(Long id, AtualizaRestauranteRequest request) {
        // busca os dados do restaurante já existentes
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));

        // busca o objeto tipo de cozinha e valida
        TipoDeCozinha tipoDeCozinha = tipoDeCozinhaRepository.findById(request.getTipoDeCozinha())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cozinha não encontrado"));

        // atualiza os dados e salva no banco atualizados
        request.atualiza(restaurante, tipoDeCozinha);
        restauranteRepository.save(restaurante);

        return restaurante;
    }

    public void remove(Long id) {
        // busca os dados do restaurante já existentes
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));

        restauranteRepository.deleteById(restaurante.getId());
    }

    // associa uma forma de pagamento a um restaurante
    public Restaurante associarFormaDePagamento(Long restauranteId, Long formaDePagamentoId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(formaDePagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));

        restaurante.getFormasDePagamento().add(formaDePagamento);
        restauranteRepository.save(restaurante);

        return restaurante;
    }

    // dessasocia uma forma de pagamento de um restaurante
    public Restaurante desassociarFormaDePagamento(Long restauranteId, Long formaDePagamentoId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não existe"));
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(formaDePagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));

        restaurante.getFormasDePagamento().remove(formaDePagamento);
        restauranteRepository.save(restaurante);

        return restaurante;
    }

    public RelatorioTipoDeCozinha gerarRelatorioPorTipoDeCozinha() {

    }
}
