package com.newbarams.ajaja.module.plan.domain.dto;

import java.time.Instant;
import java.util.List;

public class PlanResponse {

	public record GetOne(
		Long id,
		Long userId,
		String username,
		String title,
		String description,
		boolean isPublic,
		int ajajas,
		List<String> tags,
		Instant createdAt
	) {
	}

	public record GetAll(
		Long id,
		Long userId,
		String username,
		String title,
		int ajajas,
		List<String> tags,
		Instant createdAt
	) {
	}
}
