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

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

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
        model.addAttribute("espacos",  espacoRepository.findAll());
    }

    /* ── GET /tarefas/{id} — exibe a tela de detalhes ── */

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Integer id,
                           @RequestParam(required = false) String abaAtiva,
                           @RequestParam(required = false) Integer calMes,
                           @RequestParam(required = false) Integer calAno,
                           Model model) {

        Tarefa tarefa = tarefaService.buscarPorId(id)
                .orElse(null);

        if (tarefa == null) {
            adicionarDadosSidebar(model);
            model.addAttribute("erroNegocio", "Tarefa não encontrada.");
            return "erroNegocio";
        }

        /* calendário: mês/ano navegável, padrão = mês atual */
        YearMonth mesAtual = (calMes != null && calAno != null)
                ? YearMonth.of(calAno, calMes)
                : YearMonth.now();

        /* ── dados do modelo ── */
        adicionarDadosSidebar(model);
        model.addAttribute("tarefa",    tarefa);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("responsaveis",  usuarioRepository.findUsuariosByTarefaId(id));
        model.addAttribute("statusOpcoes",  Tarefa.Status.values());

        /* histórico e contadores */
        model.addAttribute("historico",     atualizacaoService.listarPorTarefa(id));
        model.addAttribute("totalNotas",    atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.NOTA));
        model.addAttribute("totalAtividades", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ATIVIDADE));
        model.addAttribute("totalArquivos", atualizacaoService.contarPorTipo(id, Atualizacao.Tipo.ARQUIVO));

        /* aba ativa (padrão: ATIVIDADE) */
        model.addAttribute("abaAtiva", abaAtiva != null ? abaAtiva : "ATIVIDADE");

        /* calendário */
        model.addAttribute("calMesAtual",   mesAtual);
        model.addAttribute("calDiasNoMes",  mesAtual.lengthOfMonth());
        model.addAttribute("calPrimeiroDia", mesAtual.atDay(1).getDayOfWeek().getValue() % 7);
        model.addAttribute("hoje",          LocalDate.now());

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
                            RedirectAttributes redirect) {
        try {
            Tarefa dados = new Tarefa();
            dados.setTitulo(titulo);
            dados.setDescricao(descricao);
            dados.setStatus(Tarefa.Status.valueOf(status));
            dados.setDataInicio(dataInicio  != null && !dataInicio.isBlank()  ? LocalDate.parse(dataInicio)  : null);
            dados.setDataEntrega(dataEntrega != null && !dataEntrega.isBlank() ? LocalDate.parse(dataEntrega) : null);

            tarefaService.atualizar(id, dados, espacoId);
            redirect.addFlashAttribute("sucesso", "Tarefa atualizada com sucesso.");

        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
        }

        return "redirect:/tarefas/" + id;
    }

    /* ── POST /tarefas/{id}/atividades — adiciona entrada no histórico ── */

    @PostMapping("/{id}/atividades")
    public String adicionarAtividade(@PathVariable Integer id,
                                     @RequestParam String descricao,
                                     @RequestParam(required = false, defaultValue = "ATIVIDADE") String tipo,
                                     RedirectAttributes redirect) {
        try {
            atualizacaoService.adicionar(id, descricao, Atualizacao.Tipo.valueOf(tipo));
            redirect.addFlashAttribute("sucesso", "Atividade registrada.");

        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erroNegocio", e.getMessage());
        }

        return "redirect:/tarefas/" + id + "?abaAtiva=" + tipo;
    }
}
