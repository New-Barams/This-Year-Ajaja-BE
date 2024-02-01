package me.ajaja.module.user.adapter.out.persistence.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;

@Getter
@Entity
@Table(name = "users")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity<UserEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false, length = 11)
	private String phoneNumber;

	@Column(nullable = false, name = "email", length = 50)
	private String signUpEmail;

	@Column(nullable = false, length = 50)
	private String remindEmail;

	@Column(nullable = false)
	private boolean verified;

	@Column(nullable = false, length = 20)
	private String remindType;

	@Embedded
	private OauthInfo oauthInfo;

	@Column(nullable = false)
	private boolean deleted;
}
