package com.newbarams.ajaja.module.plan.dto;

import java.time.Instant;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public final class PlanResponse {
	@Data
	public static class Detail {
		private final Writer writer;
		private final Long id;
		private final String title;
		private final String description;
		private final int icon;
		private final boolean isPublic;
		private final boolean canRemind;
		private final boolean canAjaja;
		private final long ajajas;
		private final List<String> tags;
		private final Instant createdAt;

		@QueryProjection
		public Detail(Writer writer, Long id, String title, String description, int icon, boolean isPublic,
			boolean canRemind, boolean canAjaja, long ajajas, List<String> tags, Instant createdAt
		) {
			this.writer = writer;
			this.id = id;
			this.title = title;
			this.description = description;
			this.icon = icon;
			this.isPublic = isPublic;
			this.canRemind = canRemind;
			this.canAjaja = canAjaja;
			this.ajajas = ajajas;
			this.tags = tags;
			this.createdAt = createdAt;
		}
	}

	@Data
	public static class Writer {
		private final String nickname;
		private final boolean isOwner;
		private final boolean isAjajaPressed;

		@QueryProjection
		public Writer(String nickname, boolean isOwner, boolean isAjajaPressed) {
			this.nickname = nickname;
			this.isOwner = isOwner;
			this.isAjajaPressed = isAjajaPressed;
		}
	}

	public record GetOne(
		Long id,
		Long userId,
		String nickname,
		String title,
		String description,
		int iconNumber,
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
		int iconNumber,
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
		int iconNumber,
		boolean isPublic,
		boolean canRemind,
		boolean canAjaja,
		int ajajas,
		List<String> tags
	) {
	}
}
