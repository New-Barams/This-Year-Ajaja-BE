package com.newbarams.ajaja.module.remind.infra;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RemindEntity extends BaseEntity<RemindEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "remind_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long planId;

	@Column(nullable = false, name = "remind_type", length = 20)
	private String type;

	@Column(nullable = false)
	private boolean deleted;

	@Column(nullable = false, length = 255)
	private String content;

	@Column(nullable = false)
	private int remindMonth;

	@Column(nullable = false)
	private int remindDay;
}
