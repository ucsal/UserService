package com.uelisson.cartoesService.repository;

import com.uelisson.cartoesService.entities.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UsuarioModel> findByLogin(String login);
    Optional<UsuarioModel> findByEmail(String email);
}