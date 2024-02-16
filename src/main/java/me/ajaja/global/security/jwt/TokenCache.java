package me.ajaja.global.security.jwt;

public interface TokenCache {
	void save(String key, String refreshToken, long expireIn);

	void validateHistory(String key, String refreshToken);

	boolean remove(String key);
}
