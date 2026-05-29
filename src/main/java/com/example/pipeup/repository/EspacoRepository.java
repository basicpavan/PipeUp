package com.example.pipeup.repository;

import com.example.pipeup.model.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, Integer> {
    List<Espaco> findByEmpresaId(Integer empresaId);
}