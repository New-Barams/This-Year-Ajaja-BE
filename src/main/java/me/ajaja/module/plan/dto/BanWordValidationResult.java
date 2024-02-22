package me.ajaja.module.plan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class BanWordValidationResult {
	private final Common title;
	private final Common description;

	@Data
	public static class Common {
		private final boolean existBanWord;
		private final List<String> banWords;
	}
}
