package me.ajaja.global.cache;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record EmailVerification(
	String email,
	String certification
) {
}
