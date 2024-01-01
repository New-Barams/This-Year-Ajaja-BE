package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

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
		int remindTotalPeriod;
		int remindTerm;
		int remindDate;
		String remindTime;
		List<Message> messages;
	}

	@Data
	public static class GetAll {
		private final String sort;
		private final boolean current;
		private final Integer ajaja;
		private final Long start;
	}
}
