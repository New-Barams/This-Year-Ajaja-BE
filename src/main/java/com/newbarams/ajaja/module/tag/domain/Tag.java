package com.newbarams.ajaja.module.tag.domain;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity<Tag> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id;

	@NotNull
	private Long planId;

	@NotBlank
	@Size(max = 10)
	@Column(name = "tag_name")
	private String name;

	public Tag(Long planId, String name) {
		this.planId = planId;
		this.name = name;
		this.validateSelf();
	}
}
