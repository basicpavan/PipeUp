package com.example.pipeup.controller;

import com.example.pipeup.model.Tarefa;
import com.example.pipeup.service.TarefaService;
import com.example.pipeup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("etapa1", tarefaService.listarPorEtapa(Tarefa.Etapa.ETAPA_1));
        model.addAttribute("etapa2", tarefaService.listarPorEtapa(Tarefa.Etapa.ETAPA_2));
        model.addAttribute("etapa3", tarefaService.listarPorEtapa(Tarefa.Etapa.ETAPA_3));
        model.addAttribute("etapa4", tarefaService.listarPorEtapa(Tarefa.Etapa.ETAPA_4));
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "dashboard";
    }
}