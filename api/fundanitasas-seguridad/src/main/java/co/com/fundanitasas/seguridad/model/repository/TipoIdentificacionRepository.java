package co.com.fundanitasas.seguridad.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.fundanitasas.seguridad.model.entity.TipoIdentificacion;

@Repository
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Long> {
	
	Optional<TipoIdentificacion> findByAbreviatura(String abreviatura);
	
	boolean existsByAbreviatura(String abreviatura);
}