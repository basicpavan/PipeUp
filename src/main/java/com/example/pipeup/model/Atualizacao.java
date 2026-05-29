package com.example.pipeup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atualizacoes")
public class Atualizacao {

    /* ── Tipos de entrada no histórico ── */
    public enum Tipo {
        ATIVIDADE, NOTA, ARQUIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atualizacao")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tipo tipo = Tipo.ATIVIDADE;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "data_time", nullable = false)
    private LocalDateTime dataTime = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (dataTime == null) dataTime = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataTime() { return dataTime; }
    public void setDataTime(LocalDateTime dataTime) { this.dataTime = dataTime; }
}
