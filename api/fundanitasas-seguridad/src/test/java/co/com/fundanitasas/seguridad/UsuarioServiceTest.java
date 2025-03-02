package co.com.fundanitasas.seguridad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.fundanitasas.seguridad.model.entity.Persona;
import co.com.fundanitasas.seguridad.model.entity.Usuario;
import co.com.fundanitasas.seguridad.model.repository.UsuarioRepository;
import co.com.fundanitasas.seguridad.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
/*
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Persona persona;

    @BeforeEach
    void setUp() {
        // Crear Persona de prueba
        persona = new Persona();
        persona.setId(1L);
        persona.setNombre("James");
        persona.setApellido("Tellez");
        persona.setEmail("james@example.com");

        // Crear Usuario asociado a Persona
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("james");
        usuario.setPassword("password123");
        usuario.setPersona(persona);
    }

    @Test
    void testGuardarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario);

        assertNotNull(usuarioGuardado);
        assertEquals("james", usuarioGuardado.getUsername());
        assertEquals("James", usuarioGuardado.getPersona().getNombre());
        assertEquals("james@example.com", usuarioGuardado.getPersona().getEmail());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testObtenerUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> usuarioEncontrado = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("james", usuarioEncontrado.get().getUsername());
        assertEquals("James", usuarioEncontrado.get().getPersona().getNombre());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario, new Usuario(2L, "ana", "password456", persona));
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
    */
}