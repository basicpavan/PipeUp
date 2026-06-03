package com.example.pipeup.service;

import com.example.pipeup.model.Usuario;
import com.example.pipeup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public Usuario criar(Usuario usuario) {
        if (repo.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        return repo.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repo.findAllByOrderByCriadoEmDesc();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        u.setNome(dados.getNome());
        u.setEmail(dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            u.setSenha(dados.getSenha());
        }
        u.setTelefone(dados.getTelefone());
        u.setDataNascimento(dados.getDataNascimento());
        u.setSetor(dados.getSetor());
        u.setCargo(dados.getCargo());
        return repo.save(u);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}//package com.example.pipeup.service;
//public class UsuarioService git {
//}
