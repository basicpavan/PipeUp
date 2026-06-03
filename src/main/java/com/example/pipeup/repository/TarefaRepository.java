package com.example.pipeup.repository;

import com.example.pipeup.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByEtapa(Tarefa.Etapa etapa);
    List<Tarefa> findAllByOrderByCriadoEmDesc();
}