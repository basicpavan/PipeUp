package com.example.pipeup.controller;

import com.example.pipeup.model.Empresa;
import com.example.pipeup.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    /* ── GET /empresas/cadastrar — exibe o formulário ── */

    @GetMapping("/cadastrar")
    public String formulario(Model model) {
        logger.info("Abrindo formulário de cadastro de empresa.");
        model.addAttribute("empresa", new Empresa());
        return "FormularioEmpresa";
    }

    /* ── POST /empresas/cadastrar — salva a empresa e redireciona ── */

    @PostMapping("/cadastrar")
    public String cadastrar(
            @RequestParam String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String senha,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String supervisor,
            RedirectAttributes redirect) {

        logger.info("Recebida requisição para cadastrar empresa: nome={}", nome);

        try {
            if (nome == null || nome.trim().isEmpty()) {
                redirect.addFlashAttribute("erroNegocio", "O nome da empresa é obrigatório.");
                return "redirect:/empresas/cadastrar";
            }

            Empresa empresa = new Empresa();
            empresa.setNome(nome.trim());
            empresa.setEmail(email != null && !email.isBlank() ? email.trim() : null);
            empresa.setSenha(senha != null && !senha.isBlank() ? senha : null);
            empresa.setTelefone(telefone != null && !telefone.isBlank() ? telefone.trim() : null);
            empresa.setCnpj(cnpj != null && !cnpj.isBlank() ? cnpj.trim() : null);
            empresa.setSupervisor(supervisor != null && !supervisor.isBlank() ? supervisor.trim() : null);

            empresaRepository.save(empresa);

            logger.info("Empresa '{}' cadastrada com sucesso.", empresa.getNome());
            redirect.addFlashAttribute("sucesso", "Empresa cadastrada com sucesso!");
            return "redirect:/dashboard";

        } catch (Exception e) {
            logger.error("Erro ao cadastrar empresa: {}", e.getMessage());
            redirect.addFlashAttribute("erroNegocio", "Erro ao cadastrar empresa: " + e.getMessage());
            return "redirect:/empresas/cadastrar";
        }
    }
}