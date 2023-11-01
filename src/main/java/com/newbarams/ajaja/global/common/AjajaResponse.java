package com.newbarams.ajaja.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"success", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjajaResponse<T> {
	private final Boolean success;
	private final T data;

	public AjajaResponse(Boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public static AjajaResponse<Void> ok(Boolean success) {
		return new AjajaResponse<>(success, null);
	}

	public static <T> AjajaResponse<T> ok(T data) {
		return new AjajaResponse<>(true, data);
	}

	public static AjajaResponse<Void> noData() {
		return new AjajaResponse<>(true, null);
	}

	public Boolean getSuccess() {
		return success;
	}

	public T getData() {
		return data;
	}
}
