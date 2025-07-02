package kh.com.blog.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kh.com.blog.dto.response.UserInfoDTO;
import kh.com.blog.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

	@Value("${spring.jwt.secret}")
	private String secret;
	@Value("${spring.jwt.expiration}")
	private int expiration;

	private static final String TOKEN_PREFIX = "Bearer ";
	private SecretKey secretKey;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(UserInfoDTO user) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + expiration);

		return io.jsonwebtoken.Jwts.builder()
				.setSubject(user.getId().toString())
				.addClaims(Map.of(
						"username", user.getUsername(),
						"email", user.getEmail(),
						"role", user.getRole(),
						"isVerified", user.getIsVerified(),
						"accountLevel", user.getAccountLevel()
				))
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}


	public String extractUsername(String token) {
		try {
			return getClaims(token).getSubject();
		} catch (RuntimeException e) {
			throw new BusinessException("Invalid JWT token : " + e);
		}
	}

	public boolean validateToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("Expired token");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported token");
		} catch (MalformedJwtException e) {
			log.info("Malformed token");
		} catch (SignatureException e) {
			log.info("Invalid signature");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.");
		}
		return false;
	}

	private Claims getClaims(String token) {
		if (token.startsWith(TOKEN_PREFIX)) {
			token = token.substring(TOKEN_PREFIX.length());
		}
		return io.jsonwebtoken.Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
