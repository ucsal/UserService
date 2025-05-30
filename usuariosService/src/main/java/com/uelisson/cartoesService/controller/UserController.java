package com.uelisson.cartoesService.controller;


import com.uelisson.cartoesService.service.GestaoUsuarioService;
import com.uelisson.cartoesService.dto.UsuarioDTO;
import com.uelisson.cartoesService.entities.AdminModel;
import com.uelisson.cartoesService.entities.AlunoModel;
import com.uelisson.cartoesService.entities.ProfessorModel;
import com.uelisson.cartoesService.entities.UsuarioModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/usuarios")
public class UserController {

    private final GestaoUsuarioService gestaoUsuarioService;

    @Autowired
    public UserController(GestaoUsuarioService gestaoUsuarioService) {
        this.gestaoUsuarioService = gestaoUsuarioService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(gestaoUsuarioService.listarTodosUsuarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = gestaoUsuarioService.encontrarUsuarioPorId(id);
        return usuarioDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/alunos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> cadastrarAluno(@Valid @RequestBody AlunoModel aluno ) {
        try {
            UsuarioDTO novoAluno = gestaoUsuarioService.cadastrarAluno(aluno, "ucsal2025");
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/professores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> cadastrarProfessor(@Valid @RequestBody ProfessorModel professor) {
        try {
            UsuarioDTO novoProfessor = gestaoUsuarioService.cadastrarProfessor(professor, "ucsal2025");
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProfessor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> cadastrarAdmin(@Valid @RequestBody AdminModel admin) {
        try {
            UsuarioDTO novoAdmin = gestaoUsuarioService.cadastrarAdmin(admin, "admin_forte_senha");
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModel usuario) {
        try {
            UsuarioDTO usuarioAtualizado = gestaoUsuarioService.atualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    static class AlterarSenhaRequest {
        public String senhaAntiga;
        public String novaSenha;
    }

    @PostMapping("/alterar-senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> alterarSenha(Authentication authentication, @Valid @RequestBody AlterarSenhaRequest request) {
        String login = authentication.getName();
        boolean sucesso = gestaoUsuarioService.alterarSenhaUsuario(login, request.senhaAntiga, request.novaSenha);
        if (sucesso) {
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha antiga incorreta ou erro ao alterar.");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            gestaoUsuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
