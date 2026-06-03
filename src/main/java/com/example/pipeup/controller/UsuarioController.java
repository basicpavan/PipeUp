package com.example.pipeup.controller;

import com.example.pipeup.model.Usuario;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ── Exibe o formulário de cadastro (Thymeleaf) ──
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "FormularioUsuario";
    }

    // ── Processa o formulário Thymeleaf ──
    @PostMapping("/criar")
    public String criar(@Valid @ModelAttribute("usuario") Usuario usuario,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "FormularioUsuario";
        }
        try {
            usuarioService.criar(usuario);
            redirect.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("erroNegocio", e.getMessage());
            return "FormularioUsuario";
        }
    }

    // ── REST: listar todos ──
    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    // ── REST: criar ──
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> criarRest(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ── REST: atualizar ──
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario dados) {
        try {
            return ResponseEntity.ok(usuarioService.atualizar(id, dados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    // ── REST: deletar ──
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}