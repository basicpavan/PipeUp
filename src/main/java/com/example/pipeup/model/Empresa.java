package com.example.pipeup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer id;

    @NotBlank(message = "O nome da empresa é obrigatório")
    @Size(max = 45)
    @Column(nullable = false, length = 45)
    private String nome;

    /* ── RF-09: dados cadastrais da empresa ── */

    @Size(max = 120, message = "O endereço deve ter no máximo 120 caracteres")
    @Column(length = 120)
    private String endereco;

    /* CNPJ no formato 00.000.000/0000-00 ou 14 dígitos. Aceita nulo para
       não quebrar cadastros antigos; quando preenchido, valida o formato. */
    @Pattern(
        regexp = "^$|\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
        message = "CNPJ inválido. Use 14 dígitos ou o formato 00.000.000/0000-00"
    )
    @Column(length = 18, unique = true)
    private String cnpj;

    @Size(max = 60, message = "A função deve ter no máximo 60 caracteres")
    @Column(length = 60)
    private String funcao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
}
