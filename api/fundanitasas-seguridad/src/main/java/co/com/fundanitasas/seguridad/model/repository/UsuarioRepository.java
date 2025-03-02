package co.com.fundanitasas.seguridad.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.fundanitasas.seguridad.constants.EstadosRegistro;
import co.com.fundanitasas.seguridad.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
	Optional<Usuario> findByUsername(String username);
    
	boolean existsByUsername(String username);
	
	Optional<Usuario> findByUsernameAndEstado(String username, EstadosRegistro estado);
}