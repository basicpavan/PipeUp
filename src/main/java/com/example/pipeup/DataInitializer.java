package com.example.pipeup;

import com.example.pipeup.model.*;
import com.example.pipeup.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            EmpresaRepository empresaRepo,
            EspacoRepository espacoRepo,
            CargoRepository cargoRepo,
            SetorRepository setorRepo,
            UsuarioRepository usuarioRepo,
            TarefaRepository tarefaRepo,
            AtualizacaoRepository atualizacaoRepo) {

        return args -> {
            // Só inicializa se o banco estiver vazio
            if (empresaRepo.count() > 0) return;

            // Empresas
            Empresa emp1 = new Empresa(); emp1.setNome("TechCorp Solutions");
            Empresa emp2 = new Empresa(); emp2.setNome("InnovaTech Ltda");
            Empresa emp3 = new Empresa(); emp3.setNome("StartupXYZ");
            empresaRepo.save(emp1); empresaRepo.save(emp2); empresaRepo.save(emp3);

            // Espaços
            Espaco esp1 = new Espaco(); esp1.setNome("Desenvolvimento Web"); esp1.setEmpresa(emp1); esp1.setDataCriacao(LocalDateTime.now());
            Espaco esp2 = new Espaco(); esp2.setNome("Marketing Digital");   esp2.setEmpresa(emp1); esp2.setDataCriacao(LocalDateTime.now());
            Espaco esp3 = new Espaco(); esp3.setNome("Recursos Humanos");    esp3.setEmpresa(emp2); esp3.setDataCriacao(LocalDateTime.now());
            Espaco esp4 = new Espaco(); esp4.setNome("Vendas");               esp4.setEmpresa(emp2); esp4.setDataCriacao(LocalDateTime.now());
            Espaco esp5 = new Espaco(); esp5.setNome("Produto");              esp5.setEmpresa(emp3); esp5.setDataCriacao(LocalDateTime.now());
            espacoRepo.save(esp1); espacoRepo.save(esp2); espacoRepo.save(esp3);
            espacoRepo.save(esp4); espacoRepo.save(esp5);

            // Cargos e Setores
            Cargo c1 = new Cargo(); c1.setNomeCargo("Desenvolvedor"); cargoRepo.save(c1);
            Cargo c2 = new Cargo(); c2.setNomeCargo("Designer");      cargoRepo.save(c2);
            Cargo c3 = new Cargo(); c3.setNomeCargo("Gerente");       cargoRepo.save(c3);

            Setor s1 = new Setor(); s1.setNomeSetor("Tecnologia"); setorRepo.save(s1);
            Setor s2 = new Setor(); s2.setNomeSetor("Marketing");  setorRepo.save(s2);
            Setor s3 = new Setor(); s3.setNomeSetor("RH");         setorRepo.save(s3);

            // Usuários
            Usuario u1 = new Usuario();
            u1.setNome("Joao Silva"); u1.setEmail("joao@techcorp.com"); u1.setSenha("senha123");
            u1.setTelefone("11987654321"); u1.setDataNascimento(LocalDate.of(1990, 5, 15));
            u1.setCargo(c1); u1.setSetor(s1); usuarioRepo.save(u1);

            Usuario u2 = new Usuario();
            u2.setNome("Maria Santos"); u2.setEmail("maria@techcorp.com"); u2.setSenha("senha123");
            u2.setTelefone("11987654322"); u2.setDataNascimento(LocalDate.of(1988, 8, 22));
            u2.setCargo(c2); u2.setSetor(s1); usuarioRepo.save(u2);

            Usuario u3 = new Usuario();
            u3.setNome("Pedro Costa"); u3.setEmail("pedro@innovatech.com"); u3.setSenha("senha123");
            u3.setTelefone("11987654323"); u3.setDataNascimento(LocalDate.of(1985, 12, 10));
            u3.setCargo(c3); u3.setSetor(s2); usuarioRepo.save(u3);

            // Tarefas
            Tarefa t1 = new Tarefa();
            t1.setTitulo("Desenvolver sistema de login");
            t1.setDescricao("Implementar autenticacao de usuarios");
            t1.setStatus(Tarefa.Status.EM_ANDAMENTO); t1.setProgresso(65f);
            t1.setPrioridade(Tarefa.Prioridade.ALTA); t1.setEspaco(esp1);
            t1.setDataInicio(LocalDate.of(2025, 1, 15)); t1.setDataEntrega(LocalDate.of(2025, 6, 15));
            t1.getResponsaveis().add(u1); tarefaRepo.save(t1);

            Tarefa t2 = new Tarefa();
            t2.setTitulo("Criar landing page");
            t2.setDescricao("Desenvolver pagina inicial responsiva");
            t2.setStatus(Tarefa.Status.A_INICIAR); t2.setProgresso(0f);
            t2.setPrioridade(Tarefa.Prioridade.MEDIA); t2.setEspaco(esp1);
            t2.setDataInicio(LocalDate.of(2025, 2, 1)); t2.setDataEntrega(LocalDate.of(2025, 7, 28));
            t2.getResponsaveis().add(u2); tarefaRepo.save(t2);

            Tarefa t3 = new Tarefa();
            t3.setTitulo("Campanha redes sociais");
            t3.setDescricao("Planejar campanha no Instagram e Facebook");
            t3.setStatus(Tarefa.Status.EM_ANDAMENTO); t3.setProgresso(30f);
            t3.setPrioridade(Tarefa.Prioridade.ALTA); t3.setEspaco(esp2);
            t3.setDataInicio(LocalDate.of(2025, 1, 20)); t3.setDataEntrega(LocalDate.of(2025, 8, 1));
            t3.getResponsaveis().add(u3); tarefaRepo.save(t3);

            Tarefa t4 = new Tarefa();
            t4.setTitulo("Processo seletivo");
            t4.setDescricao("Recrutar desenvolvedores senior");
            t4.setStatus(Tarefa.Status.EM_ATRASO); t4.setProgresso(15f);
            t4.setPrioridade(Tarefa.Prioridade.ALTA); t4.setEspaco(esp3);
            t4.setDataInicio(LocalDate.of(2025, 1, 1)); t4.setDataEntrega(LocalDate.of(2025, 3, 31));
            tarefaRepo.save(t4);

            Tarefa t5 = new Tarefa();
            t5.setTitulo("Analise de vendas Q1");
            t5.setDescricao("Relatorio de vendas do primeiro trimestre");
            t5.setStatus(Tarefa.Status.CONCLUIDO); t5.setProgresso(100f);
            t5.setPrioridade(Tarefa.Prioridade.MEDIA); t5.setEspaco(esp4);
            t5.setDataInicio(LocalDate.of(2025, 1, 1)); t5.setDataEntrega(LocalDate.of(2025, 3, 30));
            t5.getResponsaveis().add(u1); tarefaRepo.save(t5);

            Tarefa t6 = new Tarefa();
            t6.setTitulo("Redesign do produto");
            t6.setDescricao("Atualizar interface do usuario");
            t6.setStatus(Tarefa.Status.A_INICIAR); t6.setProgresso(0f);
            t6.setPrioridade(Tarefa.Prioridade.BAIXA); t6.setEspaco(esp5);
            t6.setDataInicio(LocalDate.of(2025, 2, 15)); t6.setDataEntrega(LocalDate.of(2025, 9, 15));
            tarefaRepo.save(t6);

            Tarefa t7 = new Tarefa();
            t7.setTitulo("Sistema de notificacoes");
            t7.setDescricao("Implementar push notifications");
            t7.setStatus(Tarefa.Status.EM_ANDAMENTO); t7.setProgresso(45f);
            t7.setPrioridade(Tarefa.Prioridade.MEDIA); t7.setEspaco(esp1);
            t7.setDataInicio(LocalDate.of(2025, 1, 25)); t7.setDataEntrega(LocalDate.of(2025, 7, 10));
            t7.getResponsaveis().add(u1); tarefaRepo.save(t7);

            // Histórico
            Atualizacao a1 = new Atualizacao();
            a1.setTarefa(t1); a1.setUsuario(u1); a1.setTipo(Atualizacao.Tipo.ATIVIDADE);
            a1.setDescricao("Iniciado desenvolvimento da tela de login"); a1.setDataTime(LocalDateTime.now());
            atualizacaoRepo.save(a1);

            Atualizacao a2 = new Atualizacao();
            a2.setTarefa(t1); a2.setUsuario(u1); a2.setTipo(Atualizacao.Tipo.NOTA);
            a2.setDescricao("Utilizando Spring Security para autenticacao"); a2.setDataTime(LocalDateTime.now());
            atualizacaoRepo.save(a2);

            Atualizacao a3 = new Atualizacao();
            a3.setTarefa(t3); a3.setUsuario(u3); a3.setTipo(Atualizacao.Tipo.ATIVIDADE);
            a3.setDescricao("Definidas personas para a campanha"); a3.setDataTime(LocalDateTime.now());
            atualizacaoRepo.save(a3);

            Atualizacao a4 = new Atualizacao();
            a4.setTarefa(t5); a4.setUsuario(u1); a4.setTipo(Atualizacao.Tipo.ATIVIDADE);
            a4.setDescricao("Relatorio finalizado e enviado para diretoria"); a4.setDataTime(LocalDateTime.now());
            atualizacaoRepo.save(a4);
        };
    }
}
