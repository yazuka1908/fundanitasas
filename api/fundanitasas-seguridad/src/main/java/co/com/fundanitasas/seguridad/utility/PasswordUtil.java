package co.com.fundanitasas.seguridad.utility;

import java.util.Arrays;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
	
	private final PasswordEncoder passwordEncoder;
	
	public PasswordUtil(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	public String encriptarContrasenia(String contrasenia) {
		return (!Arrays.asList(null,"").contains(contrasenia)) ? passwordEncoder.encode(contrasenia) : null;
	}
	
	public boolean compararContrasenia(String contraseniaSinHash, String contraseniaConHash) {
		return (passwordEncoder.matches(contraseniaSinHash, contraseniaConHash)); 
	}
	
	
	
}
