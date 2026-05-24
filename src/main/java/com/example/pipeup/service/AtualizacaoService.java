package com.example.pipeup.service;

import com.example.pipeup.model.Atualizacao;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.AtualizacaoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AtualizacaoService {

    @Autowired
    private AtualizacaoRepository atualizacaoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    /* ── Listagem ── */

    public List<Atualizacao> listarPorTarefa(Integer tarefaId) {
        return atualizacaoRepository.findByTarefa_IdOrderByDataTimeDesc(tarefaId);
    }

    public List<Atualizacao> listarPorTarefaETipo(Integer tarefaId, Atualizacao.Tipo tipo) {
        return atualizacaoRepository.findByTarefa_IdAndTipoOrderByDataTimeDesc(tarefaId, tipo);
    }

    /* ── Contadores para as abas ── */

    public long contarPorTipo(Integer tarefaId, Atualizacao.Tipo tipo) {
        return atualizacaoRepository
                .findByTarefa_IdAndTipoOrderByDataTimeDesc(tarefaId, tipo)
                .size();
    }

    /* ── Adicionar entrada no histórico ── */

    public Atualizacao adicionar(Integer tarefaId, String descricao, Atualizacao.Tipo tipo) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da atividade é obrigatória.");
        }
        if (descricao.trim().length() > 255) {
            throw new IllegalArgumentException("A descrição deve ter no máximo 255 caracteres.");
        }

        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada."));

        Atualizacao atualizacao = new Atualizacao();
        atualizacao.setTarefa(tarefa);
        atualizacao.setTipo(tipo != null ? tipo : Atualizacao.Tipo.ATIVIDADE);
        atualizacao.setDescricao(descricao.trim());
        atualizacao.setDataTime(LocalDateTime.now());

        return atualizacaoRepository.save(atualizacao);
    }
}
