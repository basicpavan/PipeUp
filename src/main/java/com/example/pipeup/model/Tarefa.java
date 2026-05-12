package com.example.pipeup.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Integer id;

    private String descricao;

    private Float progresso;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Float getProgresso() { return progresso; }
    public void setProgresso(Float progresso) { this.progresso = progresso; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Espaco getEspaco() { return espaco; }
    public void setEspaco(Espaco espaco) { this.espaco = espaco; }
}