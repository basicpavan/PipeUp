package com.example.pipeup.controller;

import com.example.pipeup.model.Atualizacao;
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.repository.EmpresaRepository;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.UsuarioRepository;
import com.example.pipeup.service.AtualizacaoService;
import com.example.pipeup.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private AtualizacaoService atualizacaoService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /* ── Sidebar requer empresas e espacos em qualquer view ── */

    private void adicionarDadosSidebar(Model model) {
        model.addAttribute("empresas", empresaRepository.findAll());
        model.addAttribute("espacos", espacoRepository.findAll());
    }

    /* ── GET /tarefas/{id} — exibe a tela de detalhes ── */

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Integer id,
                           @RequestParam(required = false) String abaAtiva,
                           @RequestParam(required = false) Integer calMes,
                           @RequestParam(required = false) Integer calAno,
                           Model model) {
        logger.info("Requisição GET para /tarefas/{} recebida.", id);

        Tarefa tarefa = tarefaService.buscarPorId(id).orElse(null);

        if (tarefa == null) {
            logger.warn("Tarefa com ID {} não encontrada.", id);
            adicionarDadosSidebar(model);
            model.addAttribute("erroNegocio", "Tarefa não encontrada.");
            return "erroNegocio";
        }
        logger.debug("Tarefa ID {} encontrada: {}", id, tarefa.getTitulo());

        YearMonth mesAtual = (calMes != null && calAno != null)
                ? YearMonth.of(calAno, calMes)
                : YearMonth.now();

        adicionarDadosSidebar(model);
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("responsaveis", usuarioRepository.findUsuariosByTarefaId(id));
        model.addAttribute("statusOpcoes", Tarefa.Status.values());
        model.addAttribute("prioridadeOpcoes", Tarefa.Prioridade.values());

        model.addAttribute("historico", atualizacaoService.listarPorTarefa(id));
        model.addAttribute("totalNotas", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.NOTA));
        model.addAttribute("totalAtividades", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ATIVIDADE));
        model.addAttribute("totalArquivos", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ARQUIVO));

        model.addAttribute("abaAtiva", abaAtiva != null ? abaAtiva : "ATIVIDADE");
        model.addAttribute("calMesAtual", mesAtual);
        model.addAttribute("calDiasNoMes", mesAtual.lengthOfMonth());
        model.addAttribute("calPrimeiroDia", mesAtual.atDay(1).getDayOfWeek().getValue() - 1);
        model.addAttribute("hoje", LocalDate.now());

        logger.info("Retornando view 'detalhesTarefa' para tarefa ID {}.", id);
        return "detalhesTarefa";
    }

    /* ── POST /tarefas/{id} — salva edições do formulário ── */

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Integer id,
                            @RequestParam String titulo,
                            @RequestParam(required = false) String descricao,
                            @RequestParam String status,
                            @RequestParam(required = false) String dataInicio,
                            @RequestParam(required = false) String dataEntrega,
                            @RequestParam Integer espacoId,
                            @RequestParam(required = false) String prioridade,
                            @RequestParam(required = false) Float progresso,
                            RedirectAttributes redirect) {
        logger.info("Recebida requisição para atualizar tarefa ID: {}", id);
        try {
            Tarefa dados = new Tarefa();
            dados.setTitulo(titulo);
            dados.setDescricao(descricao);
            dados.setStatus(Tarefa.Status.valueOf(status));
            dados.setDataInicio(dataInicio != null && !dataInicio.isBlank() ? LocalDate.parse(dataInicio) : null);
            dados.setDataEntrega(dataEntrega != null && !dataEntrega.isBlank() ? LocalDate.parse(dataEntrega) : null);

            if (prioridade != null && !prioridade.isBlank()) {
                dados.setPrioridade(Tarefa.Prioridade.valueOf(prioridade));
            }
            if (progresso != null) {
                dados.setProgresso(progresso);
            }

            tarefaService.atualizar(id, dados, espacoId);
            redirect.addFlashAttribute("sucesso", "Tarefa atualizada com sucesso.");
            logger.info("Tarefa ID {} atualizada com sucesso.", id);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao atualizar tarefa ID {}: {}", id, e.getMessage());
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
        }

        return "redirect:/tarefas/" + id;
    }

    /* ── GET /tarefas/nova — formulário de nova tarefa ── */

    @GetMapping("/nova")
    public String novaTarefa(
            @RequestParam(required = false) String status, // ← recebe status da coluna Kanban
            Model model) {

        adicionarDadosSidebar(model);

        Tarefa tarefa = new Tarefa();

        // Pré-seleciona o status se veio como parâmetro (ex: ?status=EM_ANDAMENTO)
        if (status != null && !status.isBlank()) {
            try {
                tarefa.setStatus(Tarefa.Status.valueOf(status));
            } catch (IllegalArgumentException ignored) {
                tarefa.setStatus(Tarefa.Status.A_INICIAR); // fallback seguro
            }
        }

        model.addAttribute("tarefa", tarefa);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("statusOpcoes", Tarefa.Status.values());
        model.addAttribute("prioridadeOpcoes", Tarefa.Prioridade.values());
        return "FormularioTarefa";
    }

    /* ── POST /tarefas/nova — salva nova tarefa ── */

    @PostMapping("/nova")
    public String criarTarefa(@RequestParam String titulo,
                              @RequestParam(required = false) String descricao,
                              @RequestParam String status,
                              @RequestParam(required = false) String dataInicio,
                              @RequestParam(required = false) String dataEntrega,
                              @RequestParam(required = false) Integer espacoId,  // ← opcional
                              @RequestParam(required = false) String prioridade,
                              @RequestParam(required = false) Float progresso,
                              RedirectAttributes redirect) {
        logger.info("Criando nova tarefa: titulo={}, status={}, espacoId={}", titulo, status, espacoId);
        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setStatus(Tarefa.Status.valueOf(status));
            tarefa.setDataInicio(dataInicio != null && !dataInicio.isBlank() ? LocalDate.parse(dataInicio) : null);
            tarefa.setDataEntrega(dataEntrega != null && !dataEntrega.isBlank() ? LocalDate.parse(dataEntrega) : null);

            if (prioridade != null && !prioridade.isBlank()) {
                tarefa.setPrioridade(Tarefa.Prioridade.valueOf(prioridade));
            }
            if (progresso != null) {
                tarefa.setProgresso(progresso);
            }

            Tarefa novaTarefa = tarefaService.criar(tarefa, espacoId);
            redirect.addFlashAttribute("sucesso", "Tarefa criada com sucesso.");
            logger.info("Nova tarefa ID {} criada com sucesso.", novaTarefa.getId());
            return "redirect:/tarefas/" + novaTarefa.getId();

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao criar nova tarefa: {}", e.getMessage());
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
            return "redirect:/tarefas/nova";
        }
    }

    /* ── POST /tarefas/{id}/atividades — adiciona atividade ao histórico ── */

    @PostMapping("/{id}/atividades")
    public String adicionarAtividade(@PathVariable Integer id,
                                     @RequestParam String descricao,
                                     @RequestParam(required = false, defaultValue = "ATIVIDADE") String tipo,
                                     RedirectAttributes redirect) {
        logger.info("Adicionando atividade à tarefa ID {}: tipo={}", id, tipo);
        try {
            atualizacaoService.adicionar(id, descricao, Atualizacao.Tipo.valueOf(tipo));
            redirect.addFlashAttribute("sucesso", "Atividade registrada.");
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao adicionar atividade à tarefa ID {}: {}", id, e.getMessage());
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
        }

        return "redirect:/tarefas/" + id + "?abaAtiva=" + tipo;
    }
}