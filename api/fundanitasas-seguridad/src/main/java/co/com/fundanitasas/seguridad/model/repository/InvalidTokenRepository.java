package co.com.fundanitasas.seguridad.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.fundanitasas.seguridad.model.entity.InvalidToken;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
	boolean existsByToken(String token);
}