package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Title extends SelfValidating<Title> {
	@NotBlank
	@Size(max = 50)
	private final String title;

	@ConstructorProperties("title")
	public Title(String title) {
		this.title = title;
		this.validateSelf();
	}
}
