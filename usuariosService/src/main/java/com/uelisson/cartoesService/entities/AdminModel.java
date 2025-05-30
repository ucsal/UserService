package com.uelisson.cartoesService.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class AdminModel extends UsuarioModel {

    public AdminModel() {
        super();
    }

    public AdminModel(String login, String password, String email, String nome, String escola) {
        super(login, password, email, nome, escola);
    }

}