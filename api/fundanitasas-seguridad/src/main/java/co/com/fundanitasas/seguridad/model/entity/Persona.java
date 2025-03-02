package co.com.fundanitasas.seguridad.model.entity;


import co.com.fundanitasas.seguridad.constants.EstadosRegistro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false, foreignKey = @ForeignKey(name = "persona_tipoident_fk"))
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;
    
    @Column(nullable = false , unique = true)
    @NotNull(message = "El número de identificación es obligatorio")
    @Digits(integer = 20, fraction = 0, message = "El número de identificación debe ser un número válido")
    private Long numeroIdentificacion;
    
    @Column(nullable = false)
    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombres;

    @Column(nullable = false)
    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellidos;

    @Column(nullable = false)
    @NotNull(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @Enumerated(EnumType.STRING) // Guardar como "ACTIVO" o "INACTIVO" en BD
    @Column(nullable = false)
    private EstadosRegistro estado = EstadosRegistro.ACTIVO; // Valor por defecto
}
