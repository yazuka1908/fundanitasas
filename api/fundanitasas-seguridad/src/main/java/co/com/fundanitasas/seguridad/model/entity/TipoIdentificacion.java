package co.com.fundanitasas.seguridad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_identificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoIdentificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NotNull(message = "El nombre del tipo de identificación es obligatorio")
	@Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
	private String nombre;
	
	@Column(nullable = false, unique = true)
	@NotNull(message = "La abreviatura del tipo de identificación es obligatorio")
	@Size(min = 2, max = 4, message = "La abreviatura debe tener entre 2 y 4 caracteres")
	private String abreviatura;
}