package com.example.pipeup.controller;

import com.example.pipeup.model.Empresa;
import com.example.pipeup.model.Espaco;
import com.example.pipeup.repository.EmpresaRepository;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(required = false) String filtro) {

        model.addAttribute("empresas", empresaRepository.findAll());
        model.addAttribute("espacos", espacoRepository.findAll());

        if (filtro != null && !filtro.isBlank()) {
            model.addAttribute("tarefas",
                    tarefaRepository.findByDescricaoContaining(filtro));
        } else {
            model.addAttribute("tarefas",
                    tarefaRepository.findAll());
        }

        return "dashboard";
    }

    @PostMapping("/espacos")
    public String criarEspaco(@RequestParam String nome,
                              @RequestParam Integer empresaId) {

        Empresa empresa = empresaRepository.findById(empresaId).orElse(null);

        if (empresa == null || nome.isBlank()) {
            return "redirect:/dashboard";
        }

        Espaco espaco = new Espaco();
        espaco.setNome(nome);
        espaco.setEmpresa(empresa);
        espaco.setDataCriacao(LocalDateTime.now());

        espacoRepository.save(espaco);

        return "redirect:/dashboard";
    }

    @PostMapping("/empresas")
    public String criarEmpresa(@RequestParam String nome) {
        Empresa empresa = new Empresa();
        empresa.setNome(nome);
        empresaRepository.save(empresa);
        return "redirect:/dashboard";
    }

    @PostMapping("/espacos/excluir")
    public String excluirEspaco(@RequestParam Integer espacoId) {
        espacoRepository.deleteById(espacoId);
        return "redirect:/dashboard";
    }

    @PostMapping("/empresas/excluir")
    public String excluirEmpresa(@RequestParam Integer empresaId) {
        empresaRepository.deleteById(empresaId);
        return "redirect:/dashboard";
    }
}

/* bckp */
