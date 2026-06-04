package com.example.pipeup.repository;

import com.example.pipeup.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByEtapa(Tarefa.Etapa etapa);
    List<Tarefa> findAllByOrderByCriadoEmDesc();
}
=======
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    List<Tarefa> findByDescricaoContaining(String filtro);

    List<Tarefa> findByStatus(Tarefa.Status status);

    List<Tarefa> findByEspaco_Id(Integer espacoId);

    @Query(value = """
        SELECT t.*
        FROM tarefa t
        INNER JOIN tarefa_usuario tu ON tu.tarefa_id = t.id_tarefa
        WHERE tu.usuario_id = :usuarioId
        """, nativeQuery = true)
    List<Tarefa> findTarefasByUsuarioId(Integer usuarioId);
}
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
