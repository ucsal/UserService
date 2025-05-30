package com.uelisson.cartoesService.controller;


import com.uelisson.cartoesService.dto.AuthRequestDTO;
import com.uelisson.cartoesService.dto.AuthResponseDTO;
import com.uelisson.cartoesService.entities.UsuarioModel;
import com.uelisson.cartoesService.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uelisson.cartoesService.config.security.JwtUtil;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login ou senha incorretos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        }
        final UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(authRequest.getLogin());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado.");
        }
        UsuarioModel usuario = usuarioRepository.findByLogin(authRequest.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + authRequest.getLogin()));
        final String jwt = jwtUtil.generateToken(userDetails, usuario.getId());

        return ResponseEntity.ok(new AuthResponseDTO(jwt, usuario.getTipoDeUsuario(), usuario.getId()));
    }
}
