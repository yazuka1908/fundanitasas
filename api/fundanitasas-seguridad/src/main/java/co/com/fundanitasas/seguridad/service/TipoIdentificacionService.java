package co.com.fundanitasas.seguridad.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.fundanitasas.seguridad.exception.EntityAlreadyExistsException;
import co.com.fundanitasas.seguridad.model.entity.TipoIdentificacion;
import co.com.fundanitasas.seguridad.model.repository.TipoIdentificacionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoIdentificacionService {

	private final TipoIdentificacionRepository tipoIdentificacionRepository;

	public TipoIdentificacionService(TipoIdentificacionRepository tipoIdentificacionRepository) {
		this.tipoIdentificacionRepository = tipoIdentificacionRepository;
	}

	// Crear tipo identificacion
	@Transactional
	public TipoIdentificacion crearTipoIdentificacion(@Valid TipoIdentificacion tipoIdentificacion) {
		log.info("Intentando crear tipo identificación: [{} - {}]", tipoIdentificacion.getNombre(),
				tipoIdentificacion.getAbreviatura());

		boolean existe = tipoIdentificacionRepository.existsByAbreviatura(tipoIdentificacion.getAbreviatura());

		if (existe) {
			log.warn("El tipo identificación ya existe en la base de datos: [{} - {}]", tipoIdentificacion.getNombre(),
					tipoIdentificacion.getAbreviatura());
			throw new EntityAlreadyExistsException("El tipo identificación ya existe [" + tipoIdentificacion.getNombre()
					+ " - " + tipoIdentificacion.getAbreviatura() + "]");
		}

		tipoIdentificacion.setId(null); // Asegura que se genere un nuevo ID
		TipoIdentificacion tipoIdentificacionNueva = tipoIdentificacionRepository.save(tipoIdentificacion);
		log.info("Tipo identificación creada con éxito, ID: {} - {}", tipoIdentificacion.getNombre(),
				tipoIdentificacion.getAbreviatura());

		return tipoIdentificacionNueva;
	}

	// Actualizar tipo identificacion
	@Transactional
	public TipoIdentificacion actualizarTipoIdentificacion(@Valid TipoIdentificacion tipoIdentificacion) {
		log.info("Actualizando tipo identificación con ID y abreviatura: [{} {}]", tipoIdentificacion.getId(),
				tipoIdentificacion.getAbreviatura());

		if (!tipoIdentificacionRepository.existsById(tipoIdentificacion.getId())) {
			throw new EntityNotFoundException(
					"No se encontró tipo identificación para actualizar con ID y abreviatura ["
							+ tipoIdentificacion.getId() + " - " + tipoIdentificacion.getAbreviatura() + "]");
		}

		TipoIdentificacion tipoIdentificacionActualizada = tipoIdentificacionRepository.save(tipoIdentificacion);
		log.info("Tipo identificación actualizado con éxito, ID: [{} - {}]", tipoIdentificacion.getNombre(),
				tipoIdentificacion.getAbreviatura());

		return tipoIdentificacionActualizada;
	}

	// Eliminar tipo identificacion por ID
	@Transactional
	public void eliminarTipoIdentificacion(Long id) {
		log.info("Eliminando tipo identificación con ID: [{}]", id);

		if (!tipoIdentificacionRepository.existsById(id)) {
			throw new EntityNotFoundException("No se encontró tipo identificación para eliminar con ID [" + id + "]");
		}

		tipoIdentificacionRepository.deleteById(id);
		log.info("Tipo identificación eliminada con éxito.");
	}

	// Listar todos los tipos identificacion
	@Transactional(readOnly = true)
	public List<TipoIdentificacion> listarTodos() {
		return tipoIdentificacionRepository.findAll();
	}

	// Buscar tipo identificacion por ID
	@Transactional(readOnly = true)
	public TipoIdentificacion buscarPorId(Long id) {
		log.info("Buscando tipo identificación con ID: [{}]", id);
		return tipoIdentificacionRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("No se encontro la tipo identificación por el ID [" + id + "]"));
	}

	// Buscar tipo identificacion por abreviatura
	@Transactional(readOnly = true)
	public TipoIdentificacion buscarPorAbreviatura(String abreviatura) {
		log.info("Buscando tipo identificación por abreviatura: [{}]", abreviatura);

		return tipoIdentificacionRepository.findByAbreviatura(abreviatura)
				.orElseThrow(() -> new EntityNotFoundException(
						"No se encontro la tipo identificación por abreviatura [" + abreviatura + "]"));
	}
}
