package me.ajaja.global.security.jwt;

public interface TokenStorage {
	void save(String key, String refreshToken, long expireIn);

	void validateHistory(String key, String refreshToken);

	boolean remove(String key);
}
