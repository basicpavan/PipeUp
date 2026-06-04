package com.example.pipeup.repository;

import com.example.pipeup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAllByOrderByCriadoEmDesc();
}
=======
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Tarefa t JOIN t.responsaveis u WHERE t.id = :tarefaId")
    List<Usuario> findUsuariosByTarefaId(Integer tarefaId);
}
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
