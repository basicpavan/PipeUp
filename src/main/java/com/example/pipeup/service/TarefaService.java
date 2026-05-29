package com.example.pipeup.service;

import com.example.pipeup.model.Espaco;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    /* ── Busca ── */

    public Optional<Tarefa> buscarPorId(Integer id) {
        return tarefaRepository.findById(id);
    }

    /* ── Salvar / Atualizar ── */

    public Tarefa salvar(Tarefa tarefa) {
        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    public Tarefa criar(Tarefa tarefa, Integer espacoId) {
        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado."));
        
        tarefa.setEspaco(espaco);
        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Integer id, Tarefa dados, Integer espacoId) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada."));

        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado."));

        tarefa.setTitulo(dados.getTitulo());
        tarefa.setDescricao(dados.getDescricao());
        tarefa.setStatus(dados.getStatus());
        tarefa.setDataInicio(dados.getDataInicio());
        tarefa.setDataEntrega(dados.getDataEntrega());
        if (dados.getPrioridade() != null) {
            tarefa.setPrioridade(dados.getPrioridade());
        }
        if (dados.getProgresso() != null) {
            tarefa.setProgresso(dados.getProgresso());
        }
        tarefa.setEspaco(espaco);

        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    /* ── Validações ── */

    private void validar(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título da tarefa é obrigatório.");
        }
        if (tarefa.getTitulo().trim().length() > 100) {
            throw new IllegalArgumentException("O título deve ter no máximo 100 caracteres.");
        }
        if (tarefa.getDescricao() != null && tarefa.getDescricao().trim().length() > 500) {
            throw new IllegalArgumentException("A descrição deve ter no máximo 500 caracteres.");
        }
        if (tarefa.getDataInicio() != null && tarefa.getDataEntrega() != null
                && tarefa.getDataEntrega().isBefore(tarefa.getDataInicio())) {
            throw new IllegalArgumentException("A data de entrega não pode ser anterior à data de início.");
        }
        if (tarefa.getProgresso() != null && (tarefa.getProgresso() < 0 || tarefa.getProgresso() > 100)) {
            throw new IllegalArgumentException("O progresso deve ser entre 0 e 100.");
        }
        if (tarefa.getEspaco() == null) {
            throw new IllegalArgumentException("A tarefa deve estar vinculada a um espaço.");
        }

        /* sanitização */
        tarefa.setTitulo(tarefa.getTitulo().trim());
        if (tarefa.getDescricao() != null) {
            tarefa.setDescricao(tarefa.getDescricao().trim());
        }
    }
}
