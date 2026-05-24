package com.example.pipeup.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tarefa")
public class Tarefa {

    /* ── Status possíveis da tarefa ── */
    public enum Status {
        A_INICIAR, EM_ANDAMENTO, EM_ATRASO, CONCLUIDO
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

    @Column(nullable = false)
    private Float progresso = 0f;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

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
}
