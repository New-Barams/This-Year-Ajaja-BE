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

	public User(String nickname, String email) {
		this.nickname = new Nickname(nickname);
		this.email = new Email(email);
		this.isDeleted = false;
	}

	public void validateEmail(String requestEmail) {
		email.validateVerifiable(requestEmail);
	}

	public void verified(String validatedEmail) {
		this.email = email.verified(validatedEmail);
	}

	public String defaultEmail() {
		return email.getEmail();
	}

	public String getRemindEmail() {
		return email.getRemindEmail();
	}

	public void delete() {
		this.isDeleted = true;
	}

	public void updateNickname(String nickname) {
		this.nickname = new Nickname(nickname);
	}
}
