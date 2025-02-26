package co.com.fundanitasas.seguridad.model.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "persona_id", nullable = false, foreignKey = @ForeignKey(name = "usuario_persona_fk"))
	@NotNull(message = "La persona es obligatoria")
	private Persona persona;
	
	@Column(nullable = false)
	@NotNull(message = "El username es obligatorio")
    @Size(min = 2, max = 100, message = "El username debe tener entre 2 y 100 caracteres")
	private String username;
	
	@Column(nullable = false)
	@NotNull(message = "El password es obligatorio")
    @Size(min = 2, max = 100, message = "El password debe tener entre 2 y 100 caracteres")
	private String password;
	
	@Column(name = "ultima_sesion", nullable = true)
	private LocalDateTime ultimaSesion;
	
	@Enumerated(EnumType.STRING) // Guardar como "ACTIVO" o "INACTIVO" en BD
    @Column(nullable = false)
    private EstadosRegistro estado = EstadosRegistro.ACTIVO; // Valor por defecto
	
	// Métodos de UserDetails
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); // Si no manejas roles, usa una lista vacía
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
    public boolean isEnabled() {
        return this.estado == EstadosRegistro.ACTIVO; // Solo usuarios activos pueden autenticarse
    }
}
