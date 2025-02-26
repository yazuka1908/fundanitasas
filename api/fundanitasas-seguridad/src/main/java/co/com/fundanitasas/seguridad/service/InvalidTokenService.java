package co.com.fundanitasas.seguridad.service;

import org.springframework.stereotype.Service;

import co.com.fundanitasas.seguridad.model.entity.InvalidToken;
import co.com.fundanitasas.seguridad.model.repository.InvalidTokenRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvalidTokenService {

	private final InvalidTokenRepository invalidTokenRepository;

	public InvalidTokenService(InvalidTokenRepository invalidTokenRepository) {
		this.invalidTokenRepository = invalidTokenRepository;
	}

	public void invalidarToken(String token) {
		log.info("Se invalida token: [{}]", token);
		invalidTokenRepository.save(new InvalidToken(token));
	}

	public boolean esTokenValido(String token) {
		boolean result = invalidTokenRepository.existsByToken(token);
		log.info("¿El token [{}] es inválido?: [{}]", token, result);
		return result;
	}

}
