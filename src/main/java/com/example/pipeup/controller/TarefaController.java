package com.example.pipeup.controller;

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

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    // ── Exibe o formulário de nova tarefa (Thymeleaf) ──
    @GetMapping("/nova")
    public String formulario(Model model,
                             @RequestParam(required = false) String etapa) {
        Tarefa tarefa = new Tarefa();
        if (etapa != null) {
            try {
                tarefa.setEtapa(Tarefa.Etapa.valueOf(etapa));
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("etapas", Tarefa.Etapa.values());
        model.addAttribute("tipos", Tarefa.TipoTarefa.values());
        model.addAttribute("usuarios", usuarioService.listarTodos());
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
    }
}