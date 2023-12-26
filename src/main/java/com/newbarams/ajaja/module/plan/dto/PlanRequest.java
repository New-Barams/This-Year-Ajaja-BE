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

		List<Message> messages
	) {
	}

	@Data
	public static class Message {
		private final String content;
		private final int remindMonth;
		private final int remindDay;
	}

	public record Update(
		int iconNumber,

		String title,
		String description,

		boolean isPublic,
		boolean canAjaja,

		List<String> tags
	) {
	}

	@Data
	public static class UpdateRemind {
		int remindTotalPeriod;
		int remindTerm;
		int remindDate;
		String remindTime;
		List<Message> messages;
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
