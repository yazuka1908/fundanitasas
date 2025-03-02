package co.com.fundanitasas.seguridad.service;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.fundanitasas.seguridad.model.entity.SesionUsuario;
import co.com.fundanitasas.seguridad.model.entity.Usuario;
import co.com.fundanitasas.seguridad.utility.JwtUtil;
import co.com.fundanitasas.seguridad.utility.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	private final SesionUsuarioService sesionUsuarioService;
	private final UsuarioService usuarioService;
	private final PasswordUtil passwordUtil;
	private final JwtUtil jwtUtil;
	private final InvalidTokenService invalidTokenService;

	public AuthService(SesionUsuarioService sesionUsuarioService, UsuarioService usuarioService,
			PasswordUtil passwordUtil, JwtUtil jwtUtil, InvalidTokenService invalidTokenService) {
		this.sesionUsuarioService = sesionUsuarioService;
		this.usuarioService = usuarioService;
		this.passwordUtil = passwordUtil;
		this.jwtUtil = jwtUtil;
		this.invalidTokenService = invalidTokenService;
	}

	@Transactional
	public String login(String username, String password, HttpServletRequest request) {

		String token = "";

		log.info("Registrando sesion del usuario: [{}]", username);
		Usuario usuario = usuarioService.buscarPorUsername(username);

		if (!passwordUtil.compararContrasenia(password, usuario.getPassword())) {
			log.warn("La contraseña es incorrecta para el usuario: [{}]", username);
			throw new BadCredentialsException("Contraseña incorrecta");
		}

		if (sesionUsuarioService.existeSesionActivaPorUsuario(usuario)) {
			token = sesionUsuarioService.obtenerTokenSesionActiva(usuario.getUsername());
			sesionUsuarioService.actualizarUltimaActividad(usuario);

		} else {
			// Generar token JWT
			token = jwtUtil.generateToken(usuario.getUsername());
			log.info("Token generado para usuario: [{}]", username);

			// Guardar sesión
			SesionUsuario sesionUsuario = sesionUsuarioService.crearSesionUsuario(usuario,
																				  obtenerIpRealCliente(request), 
																				  request.getHeader("User-Agent"), 
																				  token);
			
			log.info("sesionUsuario: [{}]", sesionUsuario.toString());
		}
		
		return token; // Genera y devuelve el JWT
	}

	@Transactional
	public void logout(String authHeader) {
		log.info("Cerrando sesion.");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new EntityNotFoundException("Token inválido o no proporcionado.");
		}

		String token = authHeader.substring(7);
		String username = jwtUtil.extractUsername(token);
		Usuario usuario = usuarioService.buscarPorUsername(username);
		SesionUsuario sesionUsuario = sesionUsuarioService.cerrarSesionUsuario(usuario);
		invalidTokenService.invalidarToken(token);
		log.info("Sesión cerrada exitosa para el usuario: [{}]", sesionUsuario);

	}

	@Transactional(readOnly = true)
	public void validaToken(String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new EntityNotFoundException("Token inválido o no proporcionado.");
		}

		String token = authHeader.substring(7);

		// Verificar si el token fue revocado (guardado en la BD como inválido)
		if (!invalidTokenService.esTokenValido(token)) {
			throw new EntityNotFoundException("Token registrado como inválido.");
		}

		// Extraer el username real del token y comparar contra el que llega
		try {
			String tokenUsername = jwtUtil.extractUsername(token);
			Usuario usuario = usuarioService.buscarPorUsername(tokenUsername);

			if (!tokenUsername.equals(usuario.getUsername())) {
				throw new EntityNotFoundException("El token no pertenece al usuario proporcionado.");
			}

			// Verificar si el token sigue siendo válido
			if (!jwtUtil.validateToken(token, tokenUsername)) {
				throw new EntityNotFoundException(
						"Token inválido o expirado, valide si pertenece al usuario en sesión.");
			}

		} catch (Exception e) {
			throw new BadCredentialsException("Error al procesar el token.");
		}
	}

	@Transactional(readOnly = true)
	public List<SesionUsuario> buscarHistorialSesionPorUsuario(String username) {
		log.info("Buscando historico sesion del usuario: [{}]", username);
		Usuario usuario = usuarioService.buscarPorUsername(username);
		return sesionUsuarioService.listarTodosPorUsuario(usuario);
	}

	private String obtenerIpRealCliente(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// Si es localhost en IPv6, convertir a IPv4
		if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
			ip = "127.0.0.1";
		}

		// Si hay múltiples IPs en "X-Forwarded-For", la primera es la real.
		if (ip != null && ip.contains(",")) {
			ip = ip.split(",")[0].trim();
		}

		return ip;
	}
}
