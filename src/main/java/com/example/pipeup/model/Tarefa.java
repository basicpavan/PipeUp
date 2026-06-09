package com.example.pipeup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tarefa")
public class Tarefa {

    /* ── Status possíveis da tarefa (RF-05) ── */
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

    /* ── Prioridade com SLA definido em horas (RF-08) ── */
    public enum Prioridade {
        BAIXA("Baixa", 72),
        MEDIA("Média", 24),
        ALTA("Alta", 8);

        private final String displayName;
        private final int horasSla;

        Prioridade(String displayName, int horasSla) {
            this.displayName = displayName;
            this.horasSla = horasSla;
        }

        public String getDisplayName() { return displayName; }

        /** Prazo (SLA) em horas para resolução conforme a prioridade. */
        public int getHorasSla() { return horasSla; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Integer id;

    @NotBlank(message = "O título da tarefa é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status = Status.A_INICIAR;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Prioridade prioridade = Prioridade.BAIXA;

    @DecimalMin(value = "0", message = "O progresso deve ser no mínimo 0")
    @DecimalMax(value = "100", message = "O progresso deve ser no máximo 100")
    @Column(nullable = false)
    private Float progresso = 0f;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    /* ── Auditoria (base para o histórico, RF-04) ── */
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

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

    /* ── Histórico de alterações desta tarefa (lado inverso, RF-04) ── */
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dataTime DESC")
    private List<Atualizacao> historico = new ArrayList<>();

    /* ── Callbacks de auditoria ── */
    @PrePersist
    public void aoCriar() {
        LocalDateTime agora = LocalDateTime.now();
        this.dataCriacao = agora;
        this.dataAtualizacao = agora;
    }

    @PreUpdate
    public void aoAtualizar() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public List<Atualizacao> getHistorico() { return historico; }
    public void setHistorico(List<Atualizacao> historico) { this.historico = historico; }

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

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Espaco getEspaco() { return espaco; }
    public void setEspaco(Espaco espaco) { this.espaco = espaco; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    /* ── Igualdade baseada no identificador ── */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarefa)) return false;
        Tarefa tarefa = (Tarefa) o;
        return id != null && id.equals(tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tarefa{id=" + id + ", titulo='" + titulo + "', status=" + status + "}";
    }
}
