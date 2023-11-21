package com.newbarams.ajaja.module.plan.dto;

import java.time.Instant;
import java.util.List;

public class PlanResponse {

	public record GetOne(
		Long id,
		Long userId,
		String nickname,
		String title,
		String description,
		boolean isPublic,
		boolean canRemind,
		boolean canAjaja,
		long ajajas,
		boolean isPressAjaja,
		List<String> tags,
		Instant createdAt
	) {
	}

	public record GetAll(
		Long id,
		Long userId,
		String nickname,
		String title,
		long ajajas,
		List<String> tags,
		Instant createdAt
	) {
	}

	public record Create(
		Long id,
		Long userId,
		String title,
		String description,
		boolean isPublic,
		boolean canRemind,
		boolean canAjaja,
		int ajajas,
		List<String> tags,
		Instant createdAt
	) {
	}
}
