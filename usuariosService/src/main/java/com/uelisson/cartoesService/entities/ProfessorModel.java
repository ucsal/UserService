package com.uelisson.cartoesService.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("PROFESSOR")
public class ProfessorModel extends UsuarioModel {


    public ProfessorModel() {
        super();
    }

    public ProfessorModel(String login, String password, String email, String nome, String escola) {
        super(login, password, email, nome, escola);
    }


}
