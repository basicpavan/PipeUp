package com.example.pipeup.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarefa")
public class Tarefa {

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
}