package co.com.fundanitasas.seguridad.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.fundanitasas.seguridad.model.entity.SesionUsuario;
import co.com.fundanitasas.seguridad.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
		String username = request.get("username");
		String password = request.get("password");
		
		// Delegar el login a AuthService (ahora maneja autenticación y sesión)
	    String token = authService.login(username, password, httpRequest);
				
		return ResponseEntity.ok(Map.of("token", token));
	}
	
	@GetMapping("/validate-token")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader,
	        									@RequestParam("username") String username) {
	    
		authService.validaToken(authHeader);
	    return ResponseEntity.status(HttpStatus.OK).body(Map.of("mensaje", "Token válido."));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
		// Delegar el login a AuthService (ahora maneja autenticación y sesión)
	    authService.logout(authHeader);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("mensaje", "Sesión cerrada exitosamente."));
	}
	
	@GetMapping("/historico-sesion/{username}")
	public ResponseEntity<List<SesionUsuario>> buscarHistorialSesionUsuario(@PathVariable("username") String username) {
	    return ResponseEntity.status(HttpStatus.OK).body(authService.buscarHistorialSesionPorUsuario(username));
	}
}
