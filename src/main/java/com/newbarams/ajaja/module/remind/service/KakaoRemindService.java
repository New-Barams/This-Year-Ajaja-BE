package com.newbarams.ajaja.module.remind.service;

public interface KakaoRemindService {
	/**
	 * 토큰 등록 API
	 */
	void registerPushToken();

	/**
	 * 토큰 삭제 API
	 */
	void deletePushToken();

	/**
	 * 푸시 알림 전송 API
	 */
	boolean pushKakaoRemind(String message);

}
