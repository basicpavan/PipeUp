package com.example.pipeup.controller;

import com.example.pipeup.model.Empresa;
import com.example.pipeup.model.Espaco;
import com.example.pipeup.model.Tarefa; // Import Tarefa to access its Status enum
import com.example.pipeup.repository.EmpresaRepository;
import com.example.pipeup.repository.EspacoRepository;
import com.example.pipeup.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List; // Importar List
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class); // Adicionar logger

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    /* ── GET / — redireciona para o dashboard ── */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(required = false) String filtro) {

        model.addAttribute("empresas", empresaRepository.findAll());
        model.addAttribute("espacos", espacoRepository.findAll());
        model.addAttribute("statusOpcoes", Tarefa.Status.values()); // Add task statuses to the model

        List<Tarefa> tarefas;
        if (filtro != null && !filtro.isBlank()) {
            logger.info("Buscando tarefas com filtro: {}", filtro);
            tarefas = tarefaRepository.findByDescricaoContaining(filtro);
        } else {
            logger.info("Buscando todas as tarefas.");
            tarefas = tarefaRepository.findAll();
        }
        logger.info("Encontradas {} tarefas para o dashboard.", tarefas.size());
        model.addAttribute("tarefas", tarefas);

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
    public String criarEmpresa(@RequestParam String nome,
                               @RequestParam(required = false) String endereco,
                               @RequestParam(required = false) String cnpj,
                               @RequestParam(required = false) String funcao) {
        if (nome == null || nome.isBlank()) {
            return "redirect:/dashboard";
        }
        Empresa empresa = new Empresa();
        empresa.setNome(nome.trim());
        empresa.setEndereco(endereco != null ? endereco.trim() : null);
        empresa.setCnpj(cnpj != null && !cnpj.isBlank() ? cnpj.trim() : null);
        empresa.setFuncao(funcao != null ? funcao.trim() : null);
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

    @PostMapping("/espacos/editar")
    public String editarEspaco(@RequestParam Integer espacoId,
                               @RequestParam String nome) {
        espacoRepository.findById(espacoId).ifPresent(espaco -> {
            if (!nome.isBlank()) {
                espaco.setNome(nome.trim());
                espacoRepository.save(espaco);
            }
        });
        return "redirect:/dashboard";
    }

    @PostMapping("/empresas/editar")
    public String editarEmpresa(@RequestParam Integer empresaId,
                                @RequestParam String nome,
                                @RequestParam(required = false) String endereco,
                                @RequestParam(required = false) String cnpj,
                                @RequestParam(required = false) String funcao) {
        empresaRepository.findById(empresaId).ifPresent(empresa -> {
            if (!nome.isBlank()) {
                empresa.setNome(nome.trim());
                empresa.setEndereco(endereco != null ? endereco.trim() : null);
                empresa.setCnpj(cnpj != null && !cnpj.isBlank() ? cnpj.trim() : null);
                empresa.setFuncao(funcao != null ? funcao.trim() : null);
                empresaRepository.save(empresa);
            }
        });
        return "redirect:/dashboard";
    }

    /* ── GET /empresas — redireciona para o dashboard ── */
    @GetMapping("/empresas")
    public String empresas() {
        return "redirect:/dashboard";
    }

    /* ── GET /usuarios — redireciona para o dashboard ── */
    @GetMapping("/usuarios")
    public String usuarios() {
        return "redirect:/dashboard";
    }
}