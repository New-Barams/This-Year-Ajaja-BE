package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.Data;

public class PlanRequest {

	public record Create(
		String title,
		String description,

		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,

		boolean isPublic,

		int iconNumber,

		List<String> tags,

		List<CreateMessage> messages
	) {
	}

	@Data
	public static class CreateMessage {
		private final String content;
		private final int remindMonth;
		private final int remindDay;
	}

	public record Update(
		String title,
		String description,

		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,

		boolean isPublic,
		boolean canRemind,
		boolean canAjaja,

		List<String> tags,

		List<String> messages
	) {
	}

	public record GetAll(
		@DefaultValue("latest")
		String sort,

		@DefaultValue("true")
		boolean current,

		Integer ajaja,
		Long start
	) {
	}
}
