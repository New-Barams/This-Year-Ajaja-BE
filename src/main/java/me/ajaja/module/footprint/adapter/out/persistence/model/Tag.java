package me.ajaja.module.footprint.adapter.out.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Tag {
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;
}
