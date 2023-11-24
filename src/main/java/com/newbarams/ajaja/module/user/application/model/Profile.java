package com.newbarams.ajaja.module.user.application.model;

/**
 * 다양한 Oauth2 대비용 추상체, 서비스에서는 유저의 이메일 정보만 필요하다.
 */
public interface Profile {
	String getEmail();
}
