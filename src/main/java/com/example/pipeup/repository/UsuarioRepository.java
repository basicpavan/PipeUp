package com.example.pipeup.repository;

import com.example.pipeup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query(value = """
        SELECT u.*
        FROM usuario u
        INNER JOIN tarefa_usuario tu ON tu.usuario_id = u.id_usuario
        WHERE tu.tarefa_id = :tarefaId
        """, nativeQuery = true)
    List<Usuario> findUsuariosByTarefaId(Integer tarefaId);
}
