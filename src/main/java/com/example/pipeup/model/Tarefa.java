package com.example.pipeup.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
=======
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f

@Entity
@Table(name = "tarefa")
public class Tarefa {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Column
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etapa etapa = Etapa.ETAPA_1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarefa tipo = TipoTarefa.TAREFA;

    @Column
    private String empresa;

    @Column
    private String equipe;

    @Column
    private String responsavel;

    @Column
    private String espaco;

    @Column(updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public enum Etapa {
        ETAPA_1("Etapa 1"),
        ETAPA_2("Etapa 2"),
        ETAPA_3("Etapa 3"),
        ETAPA_4("Etapa 4");

        private final String label;

        Etapa(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum TipoTarefa {
        TAREFA, DOCUMENTO, LEMBRETE
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public TipoTarefa getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarefa tipo) {
        this.tipo = tipo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getEspaco() {
        return espaco;
    }

    public void setEspaco(String espaco) {
        this.espaco = espaco;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
=======
    /* ── Status possíveis da tarefa ── */
    public enum Status {
        A_INICIAR("A Iniciar"),
        EM_ANDAMENTO("Em Andamento"),
        EM_ATRASO("Em Atraso"),
        CONCLUIDO("Concluído");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Prioridade {
        BAIXA, MEDIA, ALTA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status = Status.A_INICIAR;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Prioridade prioridade = Prioridade.BAIXA;

    @Column(nullable = false)
    private Float progresso = 0f;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    @ManyToMany
    @JoinTable(
        name = "tarefa_usuario",
        joinColumns = @JoinColumn(name = "tarefa_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> responsaveis = new ArrayList<>();

    public List<Usuario> getResponsaveis() { return responsaveis; }
    public void setResponsaveis(List<Usuario> responsaveis) { this.responsaveis = responsaveis; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Float getProgresso() { return progresso; }
    public void setProgresso(Float progresso) { this.progresso = progresso; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }

    public Espaco getEspaco() { return espaco; }
    public void setEspaco(Espaco espaco) { this.espaco = espaco; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
}