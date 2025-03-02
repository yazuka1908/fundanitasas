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

import co.com.fundanitasas.seguridad.model.entity.TipoIdentificacion;
import co.com.fundanitasas.seguridad.service.TipoIdentificacionService;

@RestController
@RequestMapping("/api/tipos-identificacion")
public class TipoIdentificacionController {

	private final TipoIdentificacionService tipoIdentificacionService;

	public TipoIdentificacionController(TipoIdentificacionService tipoIdentificacionService) {
		this.tipoIdentificacionService = tipoIdentificacionService;
	}

	// Crear tipo de identificacion
	@PostMapping
	public ResponseEntity<TipoIdentificacion> crearTipo(@RequestBody TipoIdentificacion tipoIdentificacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(tipoIdentificacionService.crearTipoIdentificacion(tipoIdentificacion));
	}

	// Actualizar tipo de identificación
	@PutMapping
	public ResponseEntity<TipoIdentificacion> actualizarTipo(@RequestBody TipoIdentificacion tipoIdentificacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(tipoIdentificacionService.actualizarTipoIdentificacion(tipoIdentificacion));
	}

	// Eliminar tipo de identificación
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarTipo(@PathVariable Long id) {
		tipoIdentificacionService.eliminarTipoIdentificacion(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	// Obtener todos los tipos de identificación
	@GetMapping
	public List<TipoIdentificacion> listarTodos() {
		return tipoIdentificacionService.listarTodos();
	}

	// Buscar tipo de identificación por ID
	@GetMapping("/{id}")
	public ResponseEntity<TipoIdentificacion> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(tipoIdentificacionService.buscarPorId(id));
	}

	// Buscar tipo de identificación por abreviatura
	@GetMapping("/abreviatura/{abreviatura}")
	public ResponseEntity<TipoIdentificacion> buscarPorAbreviatura(@PathVariable String abreviatura) {
		return ResponseEntity.status(HttpStatus.OK).body(tipoIdentificacionService.buscarPorAbreviatura(abreviatura));
	}
}