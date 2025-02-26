package co.com.fundanitasas.seguridad.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.fundanitasas.seguridad.model.entity.Persona;
import co.com.fundanitasas.seguridad.service.PersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

	private final PersonaService personaService;

	public PersonaController(PersonaService personaService) {
		this.personaService = personaService;
	}

	// Crear persona
	@PostMapping
	public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
		return ResponseEntity.status(HttpStatus.OK).body(personaService.crearPersona(persona));
	}

	// Actualizar persona
	@PutMapping
	public ResponseEntity<Persona> actualizarPersona(@RequestBody Persona persona) {
		return ResponseEntity.status(HttpStatus.OK).body(personaService.actualizarPersona(persona));
	}

	// Eliminar persona
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
		personaService.eliminarPersona(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	// Listar todas las personas
	@GetMapping
	public List<Persona> listarTodos() {
		return personaService.listarTodos();
	}

	// Buscar persona por ID
	@GetMapping("/{id}")
	public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(personaService.buscarPorId(id));
	}

	// Buscar persona por tipo y numero de identificacion
	@GetMapping("/buscar-por-tipo-y-numero/{tipoIdentificacion}/{numeroIdentificacion}")
	public ResponseEntity<Persona> buscarPorTipoNumeroIdentificacion(@PathVariable String tipoIdentificacion,
																	 @PathVariable Long numeroIdentificacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(personaService.buscarPorTipoYNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion));
	}
}