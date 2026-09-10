package br.com.cibus.repository;

import br.com.cibus.model.FormaDePagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamento, Long> {

    List<FormaDePagamento> findByNome(String nome);
    int countByNome(String nome);
}
