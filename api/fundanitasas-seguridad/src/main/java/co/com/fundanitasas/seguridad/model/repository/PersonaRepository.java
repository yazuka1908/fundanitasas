package co.com.fundanitasas.seguridad.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.fundanitasas.seguridad.constants.EstadosRegistro;
import co.com.fundanitasas.seguridad.model.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
	
	Optional<Persona> findByNumeroIdentificacion(Long numeroIdentificacion);
	
	boolean existsByTipoIdentificacionIdAndNumeroIdentificacion(Long tipoIdentificacionId, Long numeroIdentificacion);
	
	Optional<Persona> findByTipoIdentificacionIdAndNumeroIdentificacion(Long tipoIdentificacionId,
																		Long numeroIdentificacion);
	
	List<Persona> findByEstado(EstadosRegistro estado);
}