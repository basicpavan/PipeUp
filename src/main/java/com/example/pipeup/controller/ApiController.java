package com.example.pipeup.controller;

import com.example.pipeup.model.Empresa;
import com.example.pipeup.model.Espaco;
import com.example.pipeup.repository.EmpresaRepository;
import com.example.pipeup.repository.EspacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    /* ── API para criar empresa via AJAX ── */
    @PostMapping("/empresas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> criarEmpresa(@RequestParam String nome) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (nome == null || nome.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nome da empresa é obrigatório");
                return ResponseEntity.badRequest().body(response);
            }

            Empresa empresa = new Empresa();
            empresa.setNome(nome.trim());
            
            empresa = empresaRepository.save(empresa);
            
            response.put("success", true);
            response.put("message", "Empresa criada com sucesso");
            response.put("empresa", Map.of(
                "id", empresa.getId(),
                "nome", empresa.getNome()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao criar empresa: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /* ── API para criar espaço via AJAX ── */
    @PostMapping("/espacos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> criarEspaco(@RequestParam String nome,
                                                           @RequestParam Integer empresaId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (nome == null || nome.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nome do espaço é obrigatório");
                return ResponseEntity.badRequest().body(response);
            }

            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

            Espaco espaco = new Espaco();
            espaco.setNome(nome.trim());
            espaco.setEmpresa(empresa);
            espaco.setDataCriacao(LocalDateTime.now());
            
            espaco = espacoRepository.save(espaco);
            
            response.put("success", true);
            response.put("message", "Espaço criado com sucesso");
            response.put("espaco", Map.of(
                "id", espaco.getId(),
                "nome", espaco.getNome(),
                "empresaNome", empresa.getNome()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao criar espaço: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /* ── API para listar empresas ── */
    @GetMapping("/empresas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listarEmpresas() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            var empresas = empresaRepository.findAll();
            response.put("success", true);
            response.put("empresas", empresas);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao listar empresas: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /* ── API para listar espaços por empresa ── */
    @GetMapping("/espacos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listarEspacos(@RequestParam(required = false) Integer empresaId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            var espacos = empresaId != null 
                ? espacoRepository.findByEmpresaId(empresaId)
                : espacoRepository.findAll();
                
            response.put("success", true);
            response.put("espacos", espacos);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao listar espaços: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}