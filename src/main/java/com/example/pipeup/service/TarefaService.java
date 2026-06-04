package com.example.pipeup.service;

import com.example.pipeup.model.Espaco;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class TarefaService {

    private static final Logger logger = LoggerFactory.getLogger(TarefaService.class);

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    /* ── Busca ── */

    public Optional<Tarefa> buscarPorId(Integer id) {
        logger.debug("Buscando tarefa por ID: {}", id);
        return tarefaRepository.findById(id);
    }

    /* ── Criar ── */

    public Tarefa criar(Tarefa tarefa, Integer espacoId) {
        logger.info("Criando nova tarefa para espaço ID: {}", espacoId);
        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado."));

        tarefa.setEspaco(espaco);
        validar(tarefa);
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        logger.info("Tarefa criada com ID: {}", novaTarefa.getId());
        return novaTarefa;
    }

    /* ── Atualizar ── */

    public Tarefa atualizar(Integer id, Tarefa dados, Integer espacoId) {
        logger.info("Iniciando atualização da tarefa ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tarefa com ID {} não encontrada para atualização.", id);
                    return new IllegalArgumentException("Tarefa não encontrada.");
                });

        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> {
                    logger.error("Espaço com ID {} não encontrado.", espacoId);
                    return new IllegalArgumentException("Espaço não encontrado.");
                });

        if (dados.getTitulo() != null && !dados.getTitulo().trim().isEmpty()) {
            tarefa.setTitulo(dados.getTitulo());
        }
        if (dados.getDescricao() != null) {
            tarefa.setDescricao(dados.getDescricao());
        }
        if (dados.getStatus() != null) {
            tarefa.setStatus(dados.getStatus());
        }
        if (dados.getDataInicio() != null) {
            tarefa.setDataInicio(dados.getDataInicio());
        }
        if (dados.getDataEntrega() != null) {
            tarefa.setDataEntrega(dados.getDataEntrega());
        }
        if (dados.getPrioridade() != null) {
            tarefa.setPrioridade(dados.getPrioridade());
        }
        if (dados.getProgresso() != null) {
            tarefa.setProgresso(dados.getProgresso());
        }
        tarefa.setEspaco(espaco);

        validar(tarefa);
        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        logger.info("Tarefa ID {} atualizada com sucesso.", id);
        return tarefaAtualizada;
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

        tarefa.setTitulo(tarefa.getTitulo().trim());
        if (tarefa.getDescricao() != null) {
            tarefa.setDescricao(tarefa.getDescricao().trim());
        }
    }
}