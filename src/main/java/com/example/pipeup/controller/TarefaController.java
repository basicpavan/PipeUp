package com.example.pipeup.controller;

<<<<<<< HEAD
import com.example.pipeup.model.Tarefa;
import com.example.pipeup.service.TarefaService;
import com.example.pipeup.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tarefa")
public class TarefaController {

=======
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

import java.time.LocalDate;
import java.time.YearMonth;
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class); // Adicionar logger

>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    @Autowired
    private TarefaService tarefaService;

    @Autowired
<<<<<<< HEAD
    private UsuarioService usuarioService;

    /* ── GET /tarefas/nova — formulário de nova tarefa ── */

    @GetMapping("/nova")
    public String novaTarefa(
            @RequestParam(required = false) String status, // ← NOVO: recebe o status da coluna
            Model model) {

        adicionarDadosSidebar(model);

        Tarefa tarefa = new Tarefa();

        // ← NOVO: pré-seleciona o status se foi passado como parâmetro
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

    // ── Processa o formulário Thymeleaf ──
    @PostMapping("/criar")
    public String criar(@Valid @ModelAttribute("tarefa") Tarefa tarefa,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("etapas", Tarefa.Etapa.values());
            model.addAttribute("tipos", Tarefa.TipoTarefa.values());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "FormularioTarefa";
        }
        tarefaService.criar(tarefa);
        redirect.addFlashAttribute("sucesso", "Tarefa criada com sucesso!");
        return "redirect:/dashboard";
    }

    // ── REST: listar todas ──
    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Tarefa> listar() {
        return tarefaService.listarTodas();
    }

    // ── REST: criar (usado pelo JS do dashboard) ──
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> criarRest(@RequestBody Tarefa tarefa) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.criar(tarefa));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ── REST: atualizar ──
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Tarefa dados) {
        try {
            return ResponseEntity.ok(tarefaService.atualizar(id, dados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    // ── REST: deletar ──
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
=======
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
        model.addAttribute("espacos",  espacoRepository.findAll());
    }

    /* ── GET /tarefas/{id} — exibe a tela de detalhes ── */

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Integer id,
                           @RequestParam(required = false) String abaAtiva,
                           @RequestParam(required = false) Integer calMes,
                           @RequestParam(required = false) Integer calAno,
                           Model model) {
        logger.info("Requisição GET para /tarefas/{} recebida.", id);

        Tarefa tarefa = tarefaService.buscarPorId(id)
                .orElse(null);

        if (tarefa == null) {
            logger.warn("Tarefa com ID {} não encontrada.", id);
            adicionarDadosSidebar(model);
            model.addAttribute("erroNegocio", "Tarefa não encontrada.");
            return "erroNegocio";
        }
        logger.debug("Tarefa ID {} encontrada: {}", id, tarefa.getTitulo());

        /* calendário: mês/ano navegável, padrão = mês atual */
        YearMonth mesAtual = (calMes != null && calAno != null)
                ? YearMonth.of(calAno, calMes)
                : YearMonth.now();
        logger.debug("Mês/Ano do calendário: {}", mesAtual);

        /* ── dados do modelo ── */
        adicionarDadosSidebar(model);
        model.addAttribute("tarefa",    tarefa);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("responsaveis",  usuarioRepository.findUsuariosByTarefaId(id));
        model.addAttribute("statusOpcoes",     Tarefa.Status.values());
        model.addAttribute("prioridadeOpcoes", Tarefa.Prioridade.values());
        logger.debug("Dados básicos da tarefa e opções adicionados ao modelo.");

        /* histórico e contadores */
        model.addAttribute("historico",     atualizacaoService.listarPorTarefa(id));
        model.addAttribute("totalNotas",    atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.NOTA));
        model.addAttribute("totalAtividades", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ATIVIDADE));
        model.addAttribute("totalArquivos", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ARQUIVO));
        logger.debug("Histórico e contadores adicionados ao modelo.");

        /* aba ativa (padrão: ATIVIDADE) */
        model.addAttribute("abaAtiva", abaAtiva != null ? abaAtiva : "ATIVIDADE");
        logger.debug("Aba ativa: {}", model.getAttribute("abaAtiva"));

        /* calendário — semana começa na segunda (DayOfWeek.MONDAY = 1, então offset = valor - 1) */
        model.addAttribute("calMesAtual",   mesAtual);
        model.addAttribute("calDiasNoMes",  mesAtual.lengthOfMonth());
        model.addAttribute("calPrimeiroDia", mesAtual.atDay(1).getDayOfWeek().getValue() - 1);
        model.addAttribute("hoje",          LocalDate.now());
        logger.debug("Dados do calendário adicionados ao modelo.");

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
        logger.info("Dados recebidos: titulo={}, status={}, espacoId={}", titulo, status, espacoId);
        try {
            Tarefa dados = new Tarefa();
            dados.setTitulo(titulo);
            dados.setDescricao(descricao);
            dados.setStatus(Tarefa.Status.valueOf(status));
            dados.setDataInicio(dataInicio  != null && !dataInicio.isBlank()  ? LocalDate.parse(dataInicio)  : null);
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
    public String novaTarefa(Model model) {
        adicionarDadosSidebar(model);
        model.addAttribute("tarefa", new Tarefa());
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
                              @RequestParam Integer espacoId,
                              @RequestParam(required = false) String prioridade,
                              @RequestParam(required = false) Float progresso,
                              RedirectAttributes redirect) {
        logger.info("Recebida requisição para criar nova tarefa: titulo={}, status={}, espacoId={}", titulo, status, espacoId);
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

    @PostMapping("/{id}/atividades")
    public String adicionarAtividade(@PathVariable Integer id,
                                     @RequestParam String descricao,
                                     @RequestParam(required = false, defaultValue = "ATIVIDADE") String tipo,
                                     RedirectAttributes redirect) {
        logger.info("Recebida requisição para adicionar atividade à tarefa ID {}: descricao={}, tipo={}", id, descricao, tipo);
        try {
            atualizacaoService.adicionar(id, descricao, Atualizacao.Tipo.valueOf(tipo));
            redirect.addFlashAttribute("sucesso", "Atividade registrada.");
            logger.info("Atividade adicionada à tarefa ID {}.", id);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao adicionar atividade à tarefa ID {}: {}", id, e.getMessage());
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
        }

        return "redirect:/tarefas/" + id + "?abaAtiva=" + tipo;
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    }
}