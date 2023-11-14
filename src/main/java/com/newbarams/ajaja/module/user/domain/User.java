package com.newbarams.ajaja.module.user.domain;

import java.util.Objects;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.global.common.error.ErrorCode;
import com.newbarams.ajaja.global.common.exception.AjajaException;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity<User> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Embedded
	private Nickname nickname;
	private Email email;

	private boolean isDeleted;

	public User(String nickname, String email) {
		this.nickname = new Nickname(nickname);
		this.email = new Email(email);
		this.isDeleted = false;
	}

	public void verifyEmail() {
		validateDoneVerification();
		this.email = email.verified();
	}

	public void validateDoneVerification() {
		if (email.isVerified()) {
			throw new AjajaException(ErrorCode.ALREADY_EMAIL_VERIFIED);
		}
	}

	public void updateRemindEmailIfDifferent(String remindEmail) {
		if (!Objects.equals(email.getRemindEmail(), remindEmail)) {
			this.email = email.newRemind(remindEmail);
		}
	}

	public String defaultEmail() {
		return email.getEmail();
	}

	public void delete() {
		this.isDeleted = true;
	}
}
