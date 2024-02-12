package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Tag extends SelfValidating<Tag> {
	@NotNull
	private final Long tagId;

	@NotBlank
	@Size(max = 10)
	private final String tagName;

	@ConstructorProperties({"tag", "tagName"})
	public Tag(Long tagId, String tagName) {
		this.tagId = tagId;
		this.tagName = tagName;
		this.validateSelf();
	}
}
