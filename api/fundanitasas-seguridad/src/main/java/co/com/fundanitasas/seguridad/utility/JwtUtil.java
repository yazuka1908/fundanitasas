package co.com.fundanitasas.seguridad.utility;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import co.com.fundanitasas.seguridad.model.repository.InvalidTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	
	private final InvalidTokenRepository invalidTokenRepository;

    private final String SECRET_KEY = "claveSecretaSuperSeguraParaJWT123456789012"; // 32+ bytes
    private final Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes())); //  Codificación segura
    
    
    public JwtUtil(InvalidTokenRepository invalidTokenRepository){
    	this.invalidTokenRepository = invalidTokenRepository;
    }
    
    //  Genera token con username y roles
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extrae username del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //  Extrae la fecha de expiración
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //  Extrae cualquier claim del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (JwtException e) {
            return null; //  Retorna null si el token es inválido
        }
    }

    //  Verifica si el token es válido
    public boolean validateToken(String token, String username) {
    	try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Verifica la firma
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String extractedUsername = claims.getSubject();
            
            // Verificamos si el token ha sido revocado
            if (invalidTokenRepository.existsByToken(token)) {
                return false; // Token fue revocado (logout)
            }

            return extractedUsername.equals(username) && !isTokenExpired(token);
            
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token no soportado: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token mal formado: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Firma inválida: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token vacío o nulo: " + e.getMessage());
        }
        return false; //  Devuelve false si hay cualquier error
    }

    //  Verifica si el token está expirado
    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }
}