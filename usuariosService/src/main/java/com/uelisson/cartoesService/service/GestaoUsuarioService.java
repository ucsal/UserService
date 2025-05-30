package com.uelisson.cartoesService.service;

import com.uelisson.cartoesService.dto.UsuarioDTO;
import com.uelisson.cartoesService.entities.AdminModel;
import com.uelisson.cartoesService.entities.AlunoModel;
import com.uelisson.cartoesService.entities.ProfessorModel;
import com.uelisson.cartoesService.entities.UsuarioModel;
import com.uelisson.cartoesService.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GestaoUsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public GestaoUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	@Transactional
	public void criarAdminPadraoSeNaoExistir() {
		String adminLogin = "admin";
		if (usuarioRepository.findByLogin(adminLogin).isEmpty()) {
			AdminModel admin = new AdminModel();
			admin.setLogin(adminLogin);
			admin.setPassword(passwordEncoder.encode("admin_forte_senha"));
			admin.setEmail("admin@example.com");
			admin.setNome("Administrador Padrão");

			usuarioRepository.save(admin);
			System.out.println(">>> Administrador padrão '" + adminLogin + "' criado com sucesso! <<<");
		} else {
			System.out.println(">>> Administrador padrão '" + adminLogin + "' já existe. Nenhuma ação necessária. <<<");
		}
	}

	private UsuarioDTO convertToDto(UsuarioModel usuario) {
		if (usuario == null) return null;
		Long grupoId = null;
		if (usuario instanceof AlunoModel) {
		}
		return new UsuarioDTO(
				usuario.getId(),
				usuario.getLogin(),
				usuario.getEmail(),
				usuario.getNome(),
				usuario.getTipoDeUsuario(),
				grupoId,
				usuario.getEscola()
		);
	}

	@Transactional(readOnly = true)
	public List<UsuarioDTO> listarTodosUsuarios() {
		return usuarioRepository.findAll().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Optional<UsuarioDTO> encontrarUsuarioPorId(Long id) {
		return usuarioRepository.findById(id).map(this::convertToDto);
	}

	@Transactional(readOnly = true)
	public Optional<UsuarioDTO> encontrarUsuarioPorLogin(String login) {
		return usuarioRepository.findByLogin(login).map(this::convertToDto);
	}


	@Transactional
	public UsuarioDTO cadastrarAluno(AlunoModel aluno, String senhaRaw) {
		if (usuarioRepository.findByLogin(aluno.getLogin()).isPresent()) {
			throw new IllegalArgumentException("Login já existe: " + aluno.getLogin());
		}
		aluno.setPassword(passwordEncoder.encode(senhaRaw));
		AlunoModel salvo = usuarioRepository.save(aluno);
		return convertToDto(salvo);
	}

	@Transactional
	public UsuarioDTO cadastrarProfessor(ProfessorModel professor, String senhaRaw) {
		if (usuarioRepository.findByLogin(professor.getLogin()).isPresent()) {
			throw new IllegalArgumentException("Login já existe: " + professor.getLogin());
		}
		professor.setPassword(passwordEncoder.encode(senhaRaw));
		ProfessorModel salvo = usuarioRepository.save(professor);
		return convertToDto(salvo);
	}

	@Transactional
	public UsuarioDTO cadastrarAdmin(AdminModel admin, String senhaRaw) {
		if (admin.getLogin().equalsIgnoreCase("admin") && usuarioRepository.findByLogin("admin").isPresent()) {
			throw new IllegalArgumentException("O usuário 'admin' padrão já existe e é gerenciado pelo sistema.");
		}
		if (usuarioRepository.findByLogin(admin.getLogin()).isPresent()) {
			throw new IllegalArgumentException("Login já existe: " + admin.getLogin());
		}
		admin.setPassword(passwordEncoder.encode(senhaRaw));
		AdminModel salvo = usuarioRepository.save(admin);
		return convertToDto(salvo);
	}

	@Transactional
	public UsuarioDTO atualizarUsuario(Long id, UsuarioModel dadosAtualizacao) {
		UsuarioModel usuarioExistente = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

		if (dadosAtualizacao.getNome() != null) {
			usuarioExistente.setNome(dadosAtualizacao.getNome());
		}
		if (dadosAtualizacao.getEmail() != null) {
			if (!usuarioExistente.getEmail().equalsIgnoreCase(dadosAtualizacao.getEmail()) &&
					usuarioRepository.findByEmail(dadosAtualizacao.getEmail()).filter(u -> !u.getId().equals(id)).isPresent()) {
				throw new IllegalArgumentException("Email já cadastrado para outro usuário.");
			}
			usuarioExistente.setEmail(dadosAtualizacao.getEmail());
		}
		if (dadosAtualizacao.getEscola() != null) {
			usuarioExistente.setEscola(dadosAtualizacao.getEscola());
		}


		UsuarioModel atualizado = usuarioRepository.save(usuarioExistente);
		return convertToDto(atualizado);
	}

	@Transactional
	public boolean alterarSenhaUsuario(String login, String senhaAntiga, String novaSenha) {
		UsuarioModel usuario = usuarioRepository.findByLogin(login)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o login: " + login));

		if (!passwordEncoder.matches(senhaAntiga, usuario.getPassword())) {
			return false;
		}
		usuario.setPassword(passwordEncoder.encode(novaSenha));
		usuarioRepository.save(usuario);
		return true;
	}

	@Transactional
	public void deletarUsuario(Long id) {
		UsuarioModel usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

		if ("admin".equalsIgnoreCase(usuario.getLogin())) {
			throw new IllegalArgumentException("O administrador padrão 'admin' não pode ser deletado.");
		}

		usuarioRepository.deleteById(id);
	}
}