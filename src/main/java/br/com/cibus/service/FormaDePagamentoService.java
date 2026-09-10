package br.com.cibus.service;

import br.com.cibus.dto.formasdepagamento.AtualizaFormaDePagamentoRequest;
import br.com.cibus.dto.formasdepagamento.NovaFormaDePagamentoRequest;
import br.com.cibus.model.FormaDePagamento;
import br.com.cibus.repository.FormaDePagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormaDePagamentoService {

    private final FormaDePagamentoRepository formaDePagamentoRepository;

    public FormaDePagamentoService(FormaDePagamentoRepository formaDePagamentoRepository) {
        this.formaDePagamentoRepository = formaDePagamentoRepository;
    }

    public List<FormaDePagamento> list() {
        return formaDePagamentoRepository.findAll();
    }

    public FormaDePagamento create(NovaFormaDePagamentoRequest request) {
        // instancia uma forma de pagamento se baseando no DTO
        FormaDePagamento novaFormaDePagamento = request.toEntity();

        // salva
        formaDePagamentoRepository.save(novaFormaDePagamento);

        return novaFormaDePagamento;
    }

    public FormaDePagamento update(Long id, AtualizaFormaDePagamentoRequest request) {
        // busca os dados já existentes
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));

        // atualiza os dados e salva no banco atualizados
        request.atualiza(formaDePagamento);
        formaDePagamentoRepository.save(formaDePagamento);

        return formaDePagamento;
    }

    public void remove(Long id) {
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não existe"));
        formaDePagamentoRepository.deleteById(formaDePagamento.getId());
    }
}
