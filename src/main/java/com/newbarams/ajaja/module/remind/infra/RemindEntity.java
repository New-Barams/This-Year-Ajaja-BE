package com.newbarams.ajaja.module.remind.infra;

import java.time.Instant;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reminds")
@Where(clause = "deleted = false")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindEntity extends BaseEntity<RemindEntity> {
	public enum Type {
		PLAN,
		AJAJA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "remind_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;
	@Column(nullable = false)
	private Long planId;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, name = "remind_type", length = 20)
	private Type type;

	@Column(nullable = false)
	private boolean deleted;

	@Column(nullable = false, length = 255)
	private String content;

	@Column(nullable = false)
	private Instant starts;
	@Column(nullable = false)
	private Instant ends;
}