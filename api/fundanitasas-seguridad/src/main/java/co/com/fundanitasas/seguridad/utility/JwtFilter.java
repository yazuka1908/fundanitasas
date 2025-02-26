package co.com.fundanitasas.seguridad.utility;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.com.fundanitasas.seguridad.model.repository.InvalidTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final InvalidTokenRepository invalidTokenRepository;
	
	public JwtFilter(JwtUtil jwtUtil, 
			         UserDetailsService userDetailsService,
			         InvalidTokenRepository invalidTokenRepository) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.invalidTokenRepository = invalidTokenRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		final String authorizationHeader = request.getHeader("Authorization");

		// Permitir acceso a la ruta de login sin token
		if (path.contains("/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Verificar si el token está presente y tiene el formato correcto
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token vacío o inválido.");
			return;
		}

		String token = authorizationHeader.substring(7);
		
		// Verificar si el token ha sido revocado (Lista Negra)
        if (invalidTokenRepository.existsByToken(token)) {
		  sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
		  return;
		}
				
		// Se extrae el usuario asociado al token
		String username;
		try {
			username = jwtUtil.extractUsername(token);
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "No se encontró usuario del Token.");
			return;
		}

		// Verificar si el token es válido
		if (!jwtUtil.validateToken(token, username)) {
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Token inválido, expirado o no pertenece al usuario.");
			return;
		}

		// Autenticar usuario si no está en el contexto de seguridad
		if (SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, 
																											  null, 
																											  userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\": \"" + message + "\"}");
		response.getWriter().flush();
	}
}