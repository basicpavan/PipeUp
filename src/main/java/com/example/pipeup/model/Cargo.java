package com.example.pipeup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer id;

    @Column(name = "nome_cargo", nullable = false, length = 45)
    private String nomeCargo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeCargo() { return nomeCargo; }
    public void setNomeCargo(String nomeCargo) { this.nomeCargo = nomeCargo; }
}
