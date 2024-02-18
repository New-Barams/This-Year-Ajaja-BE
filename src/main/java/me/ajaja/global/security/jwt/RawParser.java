package me.ajaja.global.security.jwt;

import static me.ajaja.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import me.ajaja.global.exception.AjajaException;

@Component
class RawParser {
	private final io.jsonwebtoken.JwtParser parser;

	RawParser(JwtSecretProvider jwtSecretProvider) {
		this.parser = Jwts.parser()
			.verifyWith(jwtSecretProvider.getSecretKey())
			.build();
	}

	boolean tryParse(String jwt) {
		try {
			parser.parseSignedClaims(jwt);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	<T> T parseClaimWithType(String token, String key, Class<T> type) {
		return parseClaim(token)
			.map(claims -> claims.get(key, type))
			.orElseThrow(() -> new AjajaException(INVALID_TOKEN));
	}

	private Optional<Claims> parseClaim(String jwt) {
		try {
			return Optional.ofNullable(parser.parseSignedClaims(jwt).getPayload());
		} catch (JwtException | IllegalArgumentException e) {
			handleParseException(e);
		}

		return Optional.empty();
	}

	private void handleParseException(RuntimeException exception) {
		handleBadSignature(exception);
		handleInvalidStructure(exception);
		handleExpiredToken(exception);
		handleUnsupportedToken(exception);
		handleEmptyClaim(exception);
	}

	private void handleBadSignature(RuntimeException exception) {
		if (exception instanceof SecurityException) {
			throw new AjajaException(INVALID_SIGNATURE);
		}
	}

	private void handleInvalidStructure(RuntimeException exception) {
		if (exception instanceof MalformedJwtException) {
			throw new AjajaException(INVALID_TOKEN);
		}
	}

	private void handleExpiredToken(RuntimeException exception) {
		if (exception instanceof ExpiredJwtException) {
			throw new AjajaException(EXPIRED_TOKEN);
		}
	}

	private void handleUnsupportedToken(RuntimeException exception) {
		if (exception instanceof UnsupportedJwtException) {
			throw new AjajaException(UNSUPPORTED_TOKEN);
		}
	}

	private void handleEmptyClaim(RuntimeException exception) {
		if (exception instanceof IllegalArgumentException) {
			throw new AjajaException(EMPTY_TOKEN);
		}
	}
}
