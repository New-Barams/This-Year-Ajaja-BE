package com.newbarams.ajaja.module.user.auth.model;

/**
 * 다양한 Oauth2 도입 대비용 추상체, 서비스에서는 유저의 이메일 정보만 필요하다.
 */
public interface Profile {
	String getEmail();
}
