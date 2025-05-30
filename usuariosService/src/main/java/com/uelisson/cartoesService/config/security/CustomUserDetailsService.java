package com.uelisson.cartoesService.config.security;

import com.uelisson.cartoesService.entities.UsuarioModel;
import com.uelisson.cartoesService.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + username));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getTipoDeUsuario() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoDeUsuario().toUpperCase()));
        } else {

            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(usuario.getLogin(), usuario.getPassword(), authorities);
    }
}