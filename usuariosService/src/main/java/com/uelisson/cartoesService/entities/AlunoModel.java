package com.uelisson.cartoesService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALUNO")
public class AlunoModel extends UsuarioModel {

    @Column(name = "grupo_id", nullable = true)
    private Long grupoId;

    public AlunoModel() {
        super();
    }

    public AlunoModel(String login, String password, String email, String nome, String escola, Long grupoId) {
        super(login, password, email, nome, escola);
        this.grupoId = grupoId;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

}
