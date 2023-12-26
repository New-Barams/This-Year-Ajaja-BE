package com.newbarams.ajaja.module.plan.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;

public class PlanResponse {
	@Data
	public static class GetOne {
		private final Long id;

		private final Long userId;
		private final String nickname;

		private final String title;
		private final String description;

		private final int iconNumber;

		private final boolean isPublic;
		private final boolean canRemind;
		private final boolean canAjaja;

		private final long ajajas;
		private final boolean isPressAjaja;

		private final List<String> tags;

		private final Instant createdAt;
	}

	@Data
	public static class GetAll {
		private final Long id;

		private final Long userId;
		private final String nickname;

		private final String title;

		private final int iconNumber;

		private final long ajajas;

		private final List<String> tags;

		private final Instant createdAt;
	}

	@Data
	public static class Create {
		private final Long id;

		private final Long userId;

		private final String title;
		private final String description;

		private final int iconNumber;

		private final boolean isPublic;
		private final boolean canRemind;
		private final boolean canAjaja;

		private final int ajajas;

		private final List<String> tags;
	}
}
