package me.ajaja.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"success", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AjajaResponse<T>(Boolean success, T data) {

	public static <T> AjajaResponse<T> ok(T data) {
		return new AjajaResponse<>(true, data);
	}

	public static AjajaResponse<Void> ok() {
		return new AjajaResponse<>(true, null);
	}
}
