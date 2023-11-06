package com.newbarams.ajaja.module.remind.service;

/**
 * 푸시 알림 전송 API
 */
public interface KakaoPushRemind {
	boolean pushKakaoRemind(String message);
}
