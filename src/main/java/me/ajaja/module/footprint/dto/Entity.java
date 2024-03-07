package me.ajaja.module.footprint.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public final class Entity {
	@Data
	public static class Target {
		Long id;
		String title;

		@QueryProjection
		public Target(Long id, String title) {
			this.id = id;
			this.title = title;
		}
	}

	@Data
	public static class Writer {
		Long id;
		String nickname;

		@QueryProjection
		public Writer(Long id, String nickname) {
			this.id = id;
			this.nickname = nickname;
		}
	}
}
