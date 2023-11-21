package com.newbarams.ajaja.module.plan.dto;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;

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

		List<String> messages
	) {
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
		@DefaultValue("createdAt")
		String sortCondition,

		@DefaultValue("true")
		boolean isNewYear,

		Instant cursorCreatedAt,
		Long cursorId,

		@DefaultValue("30")
		int pageSize
	) {
	}
}
