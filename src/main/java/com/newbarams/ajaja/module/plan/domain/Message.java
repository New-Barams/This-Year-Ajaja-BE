package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends SelfValidating<Message> {

	@NotBlank
	@Size(max = 255)
	private String content;

	@NotNull
	int remindMonth;

	@NotNull
	int remindDate;

	public Message(String content, int remindMonth, int remindDate) {
		this.content = content;
		this.remindMonth = remindMonth;
		this.remindDate = remindDate;
		this.validateSelf();
	}
}
