package com.newbarams.ajaja.module.user.domain;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Where(clause = "status = NORMAL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity<User> {
	enum Status {
		NORMAL, DELETED
	}

	@Embedded
	private Nickname nickname;
	private Email email;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	public User(Nickname nickname, Email email) {
		this.nickname = nickname;
		this.email = email;
		this.status = Status.NORMAL;
	}

	public void verifyEmail() {
		if (isEmailVerified()) {
			throw new IllegalStateException("already verified!");
		}

		email.verified();
	}

	public boolean isEmailVerified() {
		return email.isVerified();
	}

	Status getStatus() {
		return status;
	}
}
