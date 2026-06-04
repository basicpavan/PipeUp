package com.example.pipeup.repository;

import com.example.pipeup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Tarefa t JOIN t.responsaveis u WHERE t.id = :tarefaId")
    List<Usuario> findUsuariosByTarefaId(Integer tarefaId);
}