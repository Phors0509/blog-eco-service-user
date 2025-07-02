package kh.com.blog.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kh.com.blog.common.enumeration.Role;
import kh.com.blog.dto.response.UserInfoDTO;
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
	private SecretKey secretKey;

	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String CLAIM_USERNAME = "username";
	private static final String CLAIM_EMAIL = "email";
	private static final String CLAIM_ROLE = "role";
	private static final String CLAIM_IS_VERIFIED = "is_verified";
	private static final String CLAIM_ACCOUNT_LEVEL = "account_level";

	// This method initializes the secret key used for signing JWT tokens.
	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// This method generates a JWT token for the user with a specified expiration time.
	public String generateToken(UserInfoDTO user) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + expiration);

		return io.jsonwebtoken.Jwts.builder()
				.setSubject(user.getId().toString())
				.addClaims(Map.of(
						CLAIM_USERNAME, user.getUsername(),
						CLAIM_EMAIL, user.getEmail(),
						CLAIM_ROLE, user.getRole().name(),
						CLAIM_IS_VERIFIED, user.getIsVerified(),
						CLAIM_ACCOUNT_LEVEL, user.getAccountLevel()
				))
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	// This method generates a refresh token for the user, which is valid for 7 days.
	public String generateRefreshToken(UserInfoDTO user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000L);
		return io.jsonwebtoken.Jwts.builder()
				.setSubject(user.getId().toString())
				.addClaims(Map.of(
						CLAIM_USERNAME, user.getUsername(),
						CLAIM_EMAIL, user.getEmail(),
						CLAIM_ROLE, user.getRole().name(),
						CLAIM_IS_VERIFIED, user.getIsVerified(),
						CLAIM_ACCOUNT_LEVEL, user.getAccountLevel()
				))
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	// This method extracts user information from the JWT token and returns it as a UserInfoDTO object.
	public UserInfoDTO extractUserInfo(String token) {
		Claims claims = getClaims(token);

		UserInfoDTO user = new UserInfoDTO();
		user.setId(Long.parseLong(claims.getSubject()));
		user.setUsername((String) claims.get(CLAIM_USERNAME));
		user.setEmail((String) claims.get(CLAIM_EMAIL));
		user.setRole(Role.valueOf((String) claims.get(CLAIM_ROLE)));
		user.setIsVerified((Boolean) claims.get(CLAIM_IS_VERIFIED));
		user.setAccountLevel((String) claims.get(CLAIM_ACCOUNT_LEVEL));

		return user;
	}

	// This method validates the JWT token and checks for its expiration, signature, and format.
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
		} catch (io.jsonwebtoken.security.SecurityException e) {
			log.info("Invalid signature");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.");
		}
		return false;
	}

	// This method retrieves the claims from the JWT token, which contains user information and token metadata.
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
