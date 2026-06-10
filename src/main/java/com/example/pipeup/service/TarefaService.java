package com.example.pipeup.service;

import com.example.pipeup.model.Atualizacao;
import com.example.pipeup.model.Espaco;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.AtualizacaoRepository;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class TarefaService {

    private static final Logger logger = LoggerFactory.getLogger(TarefaService.class);

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private AtualizacaoRepository atualizacaoRepository;

    @Autowired
    private AtualizacaoService atualizacaoService;

    /* ── Busca ── */

    public Optional<Tarefa> buscarPorId(Integer id) {
        logger.debug("Buscando tarefa por ID: {}", id);
        return tarefaRepository.findById(id);
    }

    /* ── Criar ── */

    /**
     * Cria uma nova tarefa.
     * espacoId é opcional: se nulo ou não encontrado, a tarefa é salva sem espaço.
     */
    public Tarefa criar(Tarefa tarefa, Integer espacoId) {
        logger.info("Criando nova tarefa. espacoId={}", espacoId);

        if (espacoId != null) {
            Optional<Espaco> espaco = espacoRepository.findById(espacoId);
            if (espaco.isPresent()) {
                tarefa.setEspaco(espaco.get());
            } else {
                logger.warn("Espaço ID {} não encontrado — tarefa será criada sem espaço.", espacoId);
            }
        }

        validar(tarefa);
        Tarefa nova = tarefaRepository.save(tarefa);
        logger.info("Tarefa criada com ID: {}", nova.getId());
        return nova;
    }

    /* ── Atualizar ── */

    @Transactional
    public Tarefa atualizar(Integer id, Tarefa dados, Integer espacoId) {
        logger.info("Atualizando tarefa ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tarefa ID {} não encontrada.", id);
                    return new IllegalArgumentException("Tarefa não encontrada.");
                });

        // Estado anterior — usado para registrar no histórico o que mudou
        Tarefa.Status statusAntigo      = tarefa.getStatus();
        Tarefa.Prioridade prioridadeAnt = tarefa.getPrioridade();
        Float progressoAntigo           = tarefa.getProgresso();
        LocalDate entregaAntiga         = tarefa.getDataEntrega();
        String espacoAntigo             = tarefa.getEspaco() != null ? tarefa.getEspaco().getNome() : null;

        if (espacoId != null) {
            Espaco espaco = espacoRepository.findById(espacoId)
                    .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado."));
            tarefa.setEspaco(espaco);
        }

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

        validar(tarefa);
        Tarefa atualizada = tarefaRepository.save(tarefa);
        logger.info("Tarefa ID {} atualizada.", id);

        // Registra no histórico cada campo que mudou
        if (atualizada.getStatus() != statusAntigo) {
            registrarHistorico(id, "Status alterado de " + nomeStatus(statusAntigo)
                    + " para " + nomeStatus(atualizada.getStatus()));
        }
        if (atualizada.getPrioridade() != prioridadeAnt) {
            registrarHistorico(id, "Prioridade alterada de " + nomePrioridade(prioridadeAnt)
                    + " para " + nomePrioridade(atualizada.getPrioridade()));
        }
        if (!Objects.equals(atualizada.getProgresso(), progressoAntigo)) {
            registrarHistorico(id, "Progresso: " + progressoAntigo + "% → " + atualizada.getProgresso() + "%");
        }
        if (!Objects.equals(atualizada.getDataEntrega(), entregaAntiga)) {
            registrarHistorico(id, "Data de entrega alterada de " + entregaAntiga
                    + " para " + atualizada.getDataEntrega());
        }
        String espacoNovo = atualizada.getEspaco() != null ? atualizada.getEspaco().getNome() : null;
        if (!Objects.equals(espacoNovo, espacoAntigo)) {
            registrarHistorico(id, "Espaço alterado de " + espacoAntigo + " para " + espacoNovo);
        }

        return atualizada;
    }

    /* ── Histórico automático ── */

    private static String nomeStatus(Tarefa.Status s) {
        return s != null ? s.getDisplayName() : "—";
    }

    private static String nomePrioridade(Tarefa.Prioridade p) {
        return p != null ? p.name() : "—";
    }

    private void registrarHistorico(Integer tarefaId, String descricao) {
        try {
            if (descricao != null && descricao.length() > 255) {
                descricao = descricao.substring(0, 255);
            }
            atualizacaoService.adicionar(tarefaId, descricao, Atualizacao.Tipo.ATIVIDADE);
        } catch (Exception e) {
            logger.warn("Não foi possível registrar histórico da tarefa ID {}: {}", tarefaId, e.getMessage());
        }
    }

    /* ── Excluir ── */

    @Transactional
    public void deletar(Integer id) {
        logger.info("Solicitada exclusão da tarefa ID: {}", id);
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tarefa ID {} não encontrada para exclusão.", id);
                    return new IllegalArgumentException("Tarefa não encontrada.");
                });

        // A entidade não tem cascade no histórico, então removemos as atualizações
        // vinculadas antes de excluir a tarefa (evita violação de chave estrangeira).
        atualizacaoRepository.deleteAll(
                atualizacaoRepository.findByTarefa_IdOrderByDataTimeDesc(id));

        tarefaRepository.delete(tarefa);
        logger.info("Tarefa ID {} excluída com sucesso.", id);
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
        if (tarefa.getProgresso() != null
                && (tarefa.getProgresso() < 0 || tarefa.getProgresso() > 100)) {
            throw new IllegalArgumentException("O progresso deve ser entre 0 e 100.");
        }

        // sanitização
        tarefa.setTitulo(tarefa.getTitulo().trim());
        if (tarefa.getDescricao() != null) {
            tarefa.setDescricao(tarefa.getDescricao().trim());
        }
    }
}