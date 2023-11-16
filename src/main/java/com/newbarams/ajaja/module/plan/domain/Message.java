package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends SelfValidating<Message> {

	@Getter
	@NotBlank
	@Size(max = 255)
	private String content;

	@PositiveOrZero
	@Column(name = "message_idx", insertable = false, updatable = false)
	private int index;

	@Getter
	boolean isSent;

	public Message(String content) {
		this.content = content;
		this.validateSelf();
	}
}
