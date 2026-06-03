package com.example.pipeup.service;

import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repo;

    public Tarefa criar(Tarefa tarefa) {
        return repo.save(tarefa);
    }

    public List<Tarefa> listarTodas() {
        return repo.findAllByOrderByCriadoEmDesc();
    }

    public List<Tarefa> listarPorEtapa(Tarefa.Etapa etapa) {
        return repo.findByEtapa(etapa);
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Tarefa atualizar(Long id, Tarefa dados) {
        Tarefa t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada: " + id));
        t.setNome(dados.getNome());
        t.setDescricao(dados.getDescricao());
        t.setEtapa(dados.getEtapa());
        t.setTipo(dados.getTipo());
        t.setEmpresa(dados.getEmpresa());
        t.setEquipe(dados.getEquipe());
        t.setResponsavel(dados.getResponsavel());
        t.setEspaco(dados.getEspaco());
        return repo.save(t);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}