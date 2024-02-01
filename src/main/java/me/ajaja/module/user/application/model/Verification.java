package me.ajaja.module.user.application.model;

/**
 * 인증 정보를 저장하는 interface 이메일과 문자를 처리한다.
 */
public interface Verification {
	String getTarget();

	String getCertification();
}
