package me.ajaja.module.plan.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.SelfValidating;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends SelfValidating<Message> {

	@NotBlank
	@Size(max = 250)
	private String content;

	@Embedded
	private RemindDate remindDate;

	public Message(String content, int remindMonth, int remindDay) {
		this.content = content;
		this.remindDate = new RemindDate(remindMonth, remindDay);
		this.validateSelf();
	}
}
