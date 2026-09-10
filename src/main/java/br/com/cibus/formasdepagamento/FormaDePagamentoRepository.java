package br.com.cibus.formasdepagamento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamento, Long> {

    List<FormaDePagamento> findByNome(String nome);
    int countByNome(String nome);
}
