package com.newbarams.ajaja.module.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
	public enum ReceiveType {
		KAKAO, EMAIL, BOTH
	}

	private final UserId userId;
	private Nickname nickname;
	private Email email;
	private ReceiveType receiveType;
	private boolean deleted;

	public static User init(String email, Long oauthId) {
		return new User(UserId.from(oauthId), Nickname.renew(), new Email(email), ReceiveType.KAKAO, false);
	}

	public void validateEmail(String requestEmail) {
		email.validateVerifiable(requestEmail);
	}

	public void verified(String validatedEmail) {
		this.email = email.verified(validatedEmail);
	}

	public void delete() {
		this.deleted = true;
	}

	public String updateNickname() {
		this.nickname = Nickname.renew();
		return nickname.getNickname();
	}

	public void updateReceive(ReceiveType receiveType) {
		this.receiveType = receiveType;
	}

	public Long getId() {
		return userId.getId();
	}

	public Long getOauthId() {
		return userId.getOauthId();
	}

	public String getEmail() {
		return email.getSignUpEmail();
	}

	public String getRemindEmail() {
		return email.getRemindEmail();
	}

	public boolean isVerified() {
		return email.isVerified();
	}
}
