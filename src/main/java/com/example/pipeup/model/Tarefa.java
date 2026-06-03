package com.example.pipeup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa")
public class Tarefa {

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
}