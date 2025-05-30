package com.uelisson.cartoesService.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_de_usuario", discriminatorType = DiscriminatorType.STRING)
public class UsuarioModel implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    private String escola;

    @Column(name = "tipo_de_usuario", insertable = false, updatable = false)
    private String tipoDeUsuario;

    public UsuarioModel() {}

    public UsuarioModel(String login, String password, String email, String nome, String escola) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.escola = escola;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEscola() { return escola; }
    public void setEscola(String escola) { this.escola = escola; }
    public String getTipoDeUsuario() { return tipoDeUsuario; }
}
