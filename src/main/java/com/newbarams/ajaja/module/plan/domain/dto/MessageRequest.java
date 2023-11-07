package com.newbarams.ajaja.module.plan.domain.dto;

public class MessageRequest {

	public record Create(
		String content,
		int index
	) {
	}
}
