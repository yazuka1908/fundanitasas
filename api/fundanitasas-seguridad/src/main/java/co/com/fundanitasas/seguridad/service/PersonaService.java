package co.com.fundanitasas.seguridad.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.fundanitasas.seguridad.model.entity.Persona;
import co.com.fundanitasas.seguridad.model.entity.TipoIdentificacion;
import co.com.fundanitasas.seguridad.model.repository.PersonaRepository;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonaService {

	private final PersonaRepository personaRepository;
	private final TipoIdentificacionService tipoIdentificacionService;

	public PersonaService(PersonaRepository personaRepository, TipoIdentificacionService tipoIdentificacionService) {
		this.personaRepository = personaRepository;
		this.tipoIdentificacionService = tipoIdentificacionService;
	}

	// Crear persona
	@Transactional
	public Persona crearPersona(@Valid Persona persona) {
		log.info("Creando persona con identificación: [{} - {}]", persona.getTipoIdentificacion().getAbreviatura(),
				persona.getNumeroIdentificacion());

		boolean existe = personaRepository.existsByTipoIdentificacionIdAndNumeroIdentificacion(
				persona.getTipoIdentificacion().getId(), persona.getNumeroIdentificacion());

		if (existe) {
			log.warn("La persona ya existe en la base de datos: [{} - {}]",
					persona.getTipoIdentificacion().getAbreviatura(), persona.getNumeroIdentificacion());
			throw new EntityNotFoundException("La persona ya existe con tipo y número de identificación");
		}

		persona.setId(null); // Asegura que se genere un nuevo ID
		Persona personaNueva = personaRepository.save(persona);
		log.info("Persona creada con éxito, ID: [{}]", personaNueva.getId());

		return personaNueva;
	}

	// Actualizar persona
	@Transactional
	public Persona actualizarPersona(@Valid Persona persona) {
		log.info("Actualizando persona con ID: [{}]", persona.getId());

		if (!personaRepository.existsById(persona.getId())) {
			throw new EntityNotFoundException(
					"No se encontró persona para actualizar con ID [" + persona.getId() + "]");
		}

		Persona personaActualizada = personaRepository.save(persona);
		log.info("Persona actualizada con éxito, ID: [{}]", personaActualizada.getId());

		return personaActualizada; // Actualiza el registro existente
	}

	// Eliminar persona
	@Transactional
	public void eliminarPersona(Long id) {
		log.info("Eliminando persona con ID: [{}]", id);

		if (!personaRepository.existsById(id)) {
			throw new EntityNotFoundException("No se encontró persona para eliminar con ID [" + id + "]");
		}

		personaRepository.deleteById(id);
		log.info("Persona eliminada con éxito.");
	}

	// Listar todas las personas
	@Transactional(readOnly = true)
	public List<Persona> listarTodos() {
		return personaRepository.findAll();
	}

	// Buscar persona por ID
	@Transactional(readOnly = true)
	public Persona buscarPorId(Long id) {
		log.info("Buscando persona con ID: {}", id);
		return personaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No se encontro la persona por el ID [" + id + "]"));
	}

	// Buscar persona por tipo y número de identificación
	@Transactional(readOnly = true)
	public Persona buscarPorTipoYNumeroIdentificacion(String tipoIdentificacion, Long numeroIdentificacion) {

		log.info("Buscando persona con tipo y numero de identificación [{} - {}]", tipoIdentificacion,
				numeroIdentificacion);

		TipoIdentificacion regTipoIdentificacion = tipoIdentificacionService.buscarPorAbreviatura(tipoIdentificacion);

		return personaRepository
				.findByTipoIdentificacionIdAndNumeroIdentificacion(regTipoIdentificacion.getId(), numeroIdentificacion)
				.orElseThrow(
						() -> new EntityNotFoundException("No se encontró la persona con los datos proporcionados."));
	}
}
