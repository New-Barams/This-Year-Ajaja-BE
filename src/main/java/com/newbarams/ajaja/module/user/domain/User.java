package com.newbarams.ajaja.module.user.domain;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

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

	public User(Nickname nickname, Email email) {
		this.nickname = nickname;
		this.email = email;
		this.isDeleted = false;
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
}
