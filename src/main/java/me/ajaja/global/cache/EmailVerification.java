package me.ajaja.global.cache;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import me.ajaja.module.user.application.model.Verification;

@JsonIgnoreProperties(ignoreUnknown = true)
record EmailVerification(
	String email,
	String certification
) implements Verification {
	@Override
	public String getTarget() {
		return email;
	}

	@Override
	public String getCertification() {
		return certification;
	}
}
