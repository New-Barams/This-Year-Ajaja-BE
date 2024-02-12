package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Ajaja extends SelfValidating<Ajaja> {
	@NotNull
	private final Long id;

	@ConstructorProperties("id")
	public Ajaja(Long id) {
		this.id = id;
		this.validateSelf();
	}
}
