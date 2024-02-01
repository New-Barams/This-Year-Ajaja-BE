package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class PlanRequest {
	@Data
	public static class Create {
		private final String title;
		private final String description;

		private final int remindTotalPeriod;
		private final int remindTerm;
		private final int remindDate;
		private final String remindTime;

		private final boolean isPublic;
		private final boolean canAjaja;

		private final int iconNumber;

		private final List<String> tags;

		private final List<Message> messages;
	}

	@Data
	public static class Message {
		private final String content;
		private final int remindMonth;
		private final int remindDay;
	}

	@Data
	public static class Update {
		private final int iconNumber;

		private final String title;
		private final String description;

		private final boolean isPublic;
		private final boolean canAjaja;

		private final List<String> tags;
	}

	@Data
	public static class UpdateRemind {
		private final int remindTotalPeriod;
		private final int remindTerm;
		private final int remindDate;
		private final String remindTime;
		private final List<Message> messages;
	}

	@Data
	public static class GetAll {
		private final String sort;
		private final boolean current;
		private final Integer ajaja;
		private final Long start;
	}

	@Data
	public static class CheckBanWord {
		@NotBlank
		private final String title;
		@NotBlank
		private final String description;
	}
}
