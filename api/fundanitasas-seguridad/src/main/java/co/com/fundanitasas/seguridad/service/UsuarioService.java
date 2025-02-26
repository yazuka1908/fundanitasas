package co.com.fundanitasas.seguridad.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.fundanitasas.seguridad.exception.EntityAlreadyExistsException;
import co.com.fundanitasas.seguridad.model.entity.Usuario;
import co.com.fundanitasas.seguridad.model.repository.UsuarioRepository;
import co.com.fundanitasas.seguridad.utility.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordUtil passwordUtil;

	public UsuarioService(UsuarioRepository usuarioRepository, 
						  PasswordUtil passwordUtil) {
		this.usuarioRepository = usuarioRepository;
		this.passwordUtil = passwordUtil;
	}

	// Crear usuario
	@Transactional
	public Usuario crearUsuario(@Valid Usuario usuario) {
		log.info("Creando el usuario: [{}]", usuario.getUsername());

		boolean existe = usuarioRepository.existsByUsername(usuario.getUsername());

		if (existe) {
			log.warn("El usuario ya existe en la base de datos: [{}]", usuario.getUsername());
			throw new EntityAlreadyExistsException("El usuario ya existe [" + usuario.getUsername() + "]");
		}

		usuario.setId(null); // Asegura que se genere un nuevo ID
		usuario = encriptarContrasenia(usuario); // Se evalua si requiere encriptarse la contraseña
		Usuario usuarioNuevo = usuarioRepository.save(usuario);
		log.info("Usuario creado con éxito: {}", usuarioNuevo.getUsername());

		return usuarioNuevo;
	}

	// Actualizar usuario
	@Transactional
	public Usuario actualizarUsuario(@Valid Usuario usuario) {
		log.info("Actualizando el usuario: [{}]", usuario.getUsername());

		if (!usuarioRepository.existsByUsername(usuario.getUsername())) {
			throw new EntityNotFoundException("No se encontró el usuario [" + usuario.getUsername() + "]");
		}

		usuario = encriptarContrasenia(usuario); // Se evalua si requiere encriptarse la contraseña
		Usuario usuarioActualizado = usuarioRepository.save(usuario);
		log.info("Usuario actualizado con éxito: [{}]", usuario.getUsername());
		return usuarioActualizado;
	}

	// Eliminar usuario por ID
	@Transactional
	public void eliminarUsuario(Long id) {
		log.info("Eliminando usuario con ID: [{}]", id);

		if (!usuarioRepository.existsById(id)) {
			throw new EntityNotFoundException("No se encontró usuario para eliminar, ID [" + id + "]");
		}

		usuarioRepository.deleteById(id);
		log.info("Usuario eliminado con éxito.");
	}

	// Listar todos los usuarios
	@Transactional(readOnly = true)
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	// Buscar usuario por ID
	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		log.info("Buscando usuario con ID: [{}]", id);
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No se encontro el usuario por el ID [" + id + "]"));
	}

	// Buscar usuario por username
	@Transactional(readOnly = true)
	public Usuario buscarPorUsername(String username) {
		log.info("Buscando usuario por username: [{}]", username);

		return usuarioRepository.findByUsername(username).orElseThrow(
				() -> new EntityNotFoundException("No se encontro el usuario por username [" + username + "]"));
	}

	// Encriptar contraseña del usuario si se requiere
	@Transactional(readOnly = true)
	public Usuario encriptarContrasenia(Usuario usuario) {

		if (usuario.getId() != null) { // Verifica si el usuario ya existe en la BD
			usuarioRepository.findById(usuario.getId()).ifPresent(usuarioExistente -> {
				// Solo encripta si la nueva contraseña es diferente de la almacenada y no está
				// encriptada
				if (!usuario.getPassword().startsWith("$2a$") && 
				    !usuario.getPassword().startsWith("$2b$") && 
				    !usuario.getPassword().startsWith("$2y$")) {
					log.info("La contraseña no está encriptada para el usuario [{}]. Encriptando...", usuario.getUsername());
					usuario.setPassword(passwordUtil.encriptarContrasenia(usuario.getPassword()));
					
				} 
			});
		} else {
			// Si el usuario es nuevo, encripta directamente la contraseña
			usuario.setPassword(passwordUtil.encriptarContrasenia(usuario.getPassword()));
		}
		return usuario;

	}
}
