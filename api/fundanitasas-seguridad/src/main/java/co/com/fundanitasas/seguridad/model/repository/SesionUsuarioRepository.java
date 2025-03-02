package co.com.fundanitasas.seguridad.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.fundanitasas.seguridad.model.entity.SesionUsuario;
import co.com.fundanitasas.seguridad.model.entity.Usuario;

public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Long> {
	
	List<SesionUsuario> findByUsuario(Usuario usuario);
	
	List<SesionUsuario> findAllByFechaFinIsNull();
	
	Optional<SesionUsuario> findTopByUsuarioUsernameOrderByFechaInicioDesc(String username);
	
	Optional<SesionUsuario> findByUsuarioAndFechaFinIsNull(Usuario usuario);
	
	Optional<SesionUsuario> findTopByUsuarioUsernameAndFechaFinIsNull(String username);
	
	boolean existsByUsuarioAndFechaFinIsNull(Usuario usuario);
	
	Optional<SesionUsuario> findByToken(String token);
}
