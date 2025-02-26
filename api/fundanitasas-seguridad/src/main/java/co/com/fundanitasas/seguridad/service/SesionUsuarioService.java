package co.com.fundanitasas.seguridad.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.fundanitasas.seguridad.model.entity.SesionUsuario;
import co.com.fundanitasas.seguridad.model.entity.Usuario;
import co.com.fundanitasas.seguridad.model.repository.SesionUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SesionUsuarioService {

	private final SesionUsuarioRepository sesionUsuarioRepository;

	public SesionUsuarioService(SesionUsuarioRepository sesionUsuarioRepository) {
		this.sesionUsuarioRepository = sesionUsuarioRepository;
	}

	// Crear sesión de usuario
	@Transactional
	public SesionUsuario crearSesionUsuario(@Valid Usuario usuario, String ip, String userAgent, String token) {
		log.info("Creando sesión para el usuario: [{}]", usuario.getUsername());
		Optional<SesionUsuario> sesionActiva = sesionUsuarioRepository.findByUsuarioAndFechaFinIsNull(usuario);

		// Si hay una sesión activa, cerrarla o mantener la misma
		if (sesionActiva.isPresent()) {
			log.info("Ya existe una sesión activa para el usuario: [{}]", usuario.getUsername());
			// cerrarSesionUsuario(sesionCerrada);
			return sesionActiva.get();
		}

		// Crear la nueva sesión
		SesionUsuario sesionNueva = sesionUsuarioRepository
				.save(new SesionUsuario(null, usuario, LocalDateTime.now(), null, token, ip, userAgent));

		log.info("Se abre nueva sesión para el usuario: [{}]", sesionNueva.getUsuario().getUsername());
		return sesionNueva;
	}

	@Transactional
	public SesionUsuario cerrarSesionUsuario(@Valid Usuario usuario) {
		log.info("Cerrando sesión para el usuario: [{}]", usuario.getUsername());
		Optional<SesionUsuario> sesionActiva = sesionUsuarioRepository.findByUsuarioAndFechaFinIsNull(usuario);

		if (sesionActiva.isEmpty()) {
			log.info("No existe sesión activa para el usuario: [{}]", usuario.getUsername());
			throw new EntityNotFoundException(
					"No existe sesión activa para el usuario: [" + usuario.getUsername() + "]");
		}

		// Si existe una sesion activa
		SesionUsuario sesion = sesionActiva.get();
		sesion.setFechaFin(LocalDateTime.now());
		SesionUsuario sesionCerrada = sesionUsuarioRepository.save(sesion);
		log.info("Cerrada sesión exitosamente para el usuario: [{}]", sesionCerrada.getUsuario().getUsername());
		return sesionCerrada;
	}

	// Listar historico de sesiones por sesionUsuario
	@Transactional(readOnly = true)
	public List<SesionUsuario> listarTodosPorUsuario(@Valid SesionUsuario sesionUsuario) {
		return sesionUsuarioRepository.findByUsuario(sesionUsuario.getUsuario());
	}

	// Listar historico de sesiones por usuario
	@Transactional(readOnly = true)
	public List<SesionUsuario> listarTodosPorUsuario(@Valid Usuario usuario) {
		return sesionUsuarioRepository.findByUsuario(usuario);
	}

	// Buscar sesion por usuario
	@Transactional(readOnly = true)
	public SesionUsuario buscarActivoPorUsername(String username) {
		log.info("Buscando sesion de usuario por username: [{}]", username);
		return sesionUsuarioRepository.findTopByUsuarioUsernameAndFechaFinIsNull(username).orElseThrow(
				() -> new EntityNotFoundException("No se encontro sesion activa para el usuario [" + username + "]"));
	}

	// Buscar sesion por usuario
	@Transactional(readOnly = true)
	public boolean existeSesionActivaPorUsuario(Usuario usuario) {
		log.info("Valida si existe una sesion activa por el usuario: [{}]", usuario.getUsername());
		return sesionUsuarioRepository.existsByUsuarioAndFechaFinIsNull(usuario);
	}
	
	// Obtener token de la sesión activa por username
	@Transactional(readOnly = true)
	public String obtenerTokenSesionActiva(String username) {
		log.info("Obtener el token de la sesion activa del usuario: [{}]", username);
		
		SesionUsuario sesionUsuarioActiva = sesionUsuarioRepository.findTopByUsuarioUsernameAndFechaFinIsNull(username).orElseThrow(
				() -> new EntityNotFoundException("No se encontro sesion activa para el usuario [" + username + "]"));
		
		log.info("Se retorna el token activo del usuario: [{} - {}]", sesionUsuarioActiva.getToken(), username);
		return (sesionUsuarioActiva.getToken() != null) ? sesionUsuarioActiva.getToken() : null;
	}

}
