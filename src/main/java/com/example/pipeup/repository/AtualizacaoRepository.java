package com.example.pipeup.repository;

import com.example.pipeup.model.Atualizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtualizacaoRepository extends JpaRepository<Atualizacao, Integer> {

    List<Atualizacao> findByTarefa_IdOrderByDataTimeDesc(Integer tarefaId);

    List<Atualizacao> findByTarefa_IdAndTipoOrderByDataTimeDesc(
            Integer tarefaId, Atualizacao.Tipo tipo);
}
