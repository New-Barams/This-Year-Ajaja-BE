package com.newbarams.ajaja.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjajaResponse<T> {
	private final Boolean success;
	private final T data;

	public AjajaResponse(Boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public static <T> AjajaResponse<T> ok(T data) {
		return new AjajaResponse<>(true, data);
	}

	public static AjajaResponse<Void> ok() {
		return new AjajaResponse<>(true, null);
	}
}
