package me.ajaja.module.plan.dto;

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
	public static class MainInfo {
		private final int year;
		private final int totalAchieveRate;
		private final List<PlanResponse.PlanInfo> getPlanList;
	}

	@Data
	public static class PlanInfo {
		private final int year;
		private final Long planId;
		private final String title;
		private final boolean remindable;
		private final int achieveRate;
		private final int icon;
	}
}
