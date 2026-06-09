package com.example.pipeup.service;

import com.example.pipeup.model.Atualizacao;
import com.example.pipeup.model.Espaco;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

@Service
public class TarefaService {

    private static final Logger logger = LoggerFactory.getLogger(TarefaService.class); // Adicionar logger

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private AtualizacaoService atualizacaoService; // RF-04: registro automático de histórico

    /* ── Busca ── */

    public Optional<Tarefa> buscarPorId(Integer id) {
        logger.debug("Buscando tarefa por ID: {}", id);
        return tarefaRepository.findById(id);
    }

    /* ── Salvar / Atualizar ── */

    public Tarefa salvar(Tarefa tarefa) {
        logger.debug("Salvando tarefa: {}", tarefa.getTitulo());
        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    public Tarefa criar(Tarefa tarefa, Integer espacoId) {
        logger.info("Criando nova tarefa para espaço ID: {}", espacoId);
        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado."));
        
        tarefa.setEspaco(espaco);
        validar(tarefa);
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        logger.info("Tarefa criada com ID: {}", novaTarefa.getId());

        // RF-04: registra a criação no histórico
        registrarHistorico(novaTarefa.getId(), "Tarefa criada: \"" + novaTarefa.getTitulo() + "\"");

        return novaTarefa;
    }

    public Tarefa atualizar(Integer id, Tarefa dados, Integer espacoId) {
        logger.info("Iniciando atualização da tarefa ID: {}", id);
        logger.debug("Dados recebidos para atualização: titulo={}, status={}, espacoId={}", dados.getTitulo(), dados.getStatus(), espacoId);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tarefa com ID {} não encontrada para atualização.", id);
                    return new IllegalArgumentException("Tarefa não encontrada.");
                });

        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> {
                    logger.error("Espaço com ID {} não encontrado para atualização da tarefa ID {}.", espacoId, id);
                    return new IllegalArgumentException("Espaço não encontrado.");
                });

        // RF-04: captura o estado anterior ANTES de aplicar as mudanças
        Tarefa.Status statusAntigo       = tarefa.getStatus();
        Tarefa.Prioridade prioridadeAnt  = tarefa.getPrioridade();
        Float progressoAntigo            = tarefa.getProgresso();
        LocalDate entregaAntiga          = tarefa.getDataEntrega();
        String espacoAntigo              = tarefa.getEspaco() != null ? tarefa.getEspaco().getNome() : null;

        // Atualiza apenas os campos que foram fornecidos (não são nulos)
        if (dados.getTitulo() != null && !dados.getTitulo().trim().isEmpty()) {
            logger.debug("Atualizando título da tarefa ID {} de '{}' para '{}'.", id, tarefa.getTitulo(), dados.getTitulo());
            tarefa.setTitulo(dados.getTitulo());
        }
        if (dados.getDescricao() != null) {
            logger.debug("Atualizando descrição da tarefa ID {}.", id);
            tarefa.setDescricao(dados.getDescricao());
        }
        if (dados.getStatus() != null) {
            logger.debug("Atualizando status da tarefa ID {} de '{}' para '{}'.", id, tarefa.getStatus(), dados.getStatus());
            tarefa.setStatus(dados.getStatus());
        }
        if (dados.getDataInicio() != null) {
            logger.debug("Atualizando data de início da tarefa ID {}.", id);
            tarefa.setDataInicio(dados.getDataInicio());
        }
        if (dados.getDataEntrega() != null) {
            logger.debug("Atualizando data de entrega da tarefa ID {}.", id);
            tarefa.setDataEntrega(dados.getDataEntrega());
        }
        if (dados.getPrioridade() != null) {
            logger.debug("Atualizando prioridade da tarefa ID {}.", id);
            tarefa.setPrioridade(dados.getPrioridade());
        }
        if (dados.getProgresso() != null) {
            logger.debug("Atualizando progresso da tarefa ID {}.", id);
            tarefa.setProgresso(dados.getProgresso());
        }
        logger.debug("Atualizando espaço da tarefa ID {} para ID {}.", id, espacoId);
        tarefa.setEspaco(espaco);

        validar(tarefa);
        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        logger.info("Tarefa ID {} atualizada e salva com sucesso.", id);

        // RF-04: registra no histórico cada campo que mudou
        if (tarefaAtualizada.getStatus() != statusAntigo) {
            registrarHistorico(id, "Status alterado de "
                    + nome(statusAntigo) + " para " + nome(tarefaAtualizada.getStatus()));
        }
        if (tarefaAtualizada.getPrioridade() != prioridadeAnt) {
            registrarHistorico(id, "Prioridade alterada de "
                    + nome(prioridadeAnt) + " para " + nome(tarefaAtualizada.getPrioridade()));
        }
        if (!Objects.equals(tarefaAtualizada.getProgresso(), progressoAntigo)) {
            registrarHistorico(id, "Progresso: " + progressoAntigo + "% → "
                    + tarefaAtualizada.getProgresso() + "%");
        }
        if (!Objects.equals(tarefaAtualizada.getDataEntrega(), entregaAntiga)) {
            registrarHistorico(id, "Data de entrega alterada de "
                    + entregaAntiga + " para " + tarefaAtualizada.getDataEntrega());
        }
        String espacoNovo = tarefaAtualizada.getEspaco() != null ? tarefaAtualizada.getEspaco().getNome() : null;
        if (!Objects.equals(espacoNovo, espacoAntigo)) {
            registrarHistorico(id, "Espaço alterado de " + espacoAntigo + " para " + espacoNovo);
        }

        return tarefaAtualizada;
    }

    /* ── Histórico (RF-04) ── */

    private static String nome(Tarefa.Status s) {
        return s != null ? s.getDisplayName() : "—";
    }

    private static String nome(Tarefa.Prioridade p) {
        return p != null ? p.getDisplayName() : "—";
    }

    /**
     * Registra uma entrada de histórico do tipo ATIVIDADE. Falhas no registro
     * não interrompem a operação principal sobre a tarefa.
     */
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
