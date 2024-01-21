package com.newbarams.ajaja.module.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
	public enum RemindType {
		KAKAO, EMAIL, BOTH
	}

	private final UserId userId;
	private Nickname nickname;
	private Email email;
	private RemindType remindType;
	private boolean deleted;

	public static User init(String email, Long oauthId) {
		return new User(UserId.from(oauthId), Nickname.init(), Email.init(email), RemindType.KAKAO, false);
	}

	public void delete() {
		this.deleted = true;
	}

	public void validateEmail(String requestEmail) {
		email.validateVerifiable(requestEmail);
	}

	public void verified(String validatedEmail) {
		this.email = email.verified(validatedEmail);
	}

	public void refreshNickname() {
		this.nickname = Nickname.refresh();
	}

	public void changeRemind(RemindType remindType) {
		this.remindType = remindType;
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
