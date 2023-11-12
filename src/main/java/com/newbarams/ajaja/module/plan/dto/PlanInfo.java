package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlanInfo {

	@RequiredArgsConstructor
	public static class PlanInfoResponse {
		@JsonProperty
		final int totalAchieveRate;
		@JsonProperty
		final List<GetPlanInfo> getPlanInfoList;
	}

	@Getter
	@RequiredArgsConstructor
	public static class GetPlanInfo {
		final String title;
		final boolean isRemindable;
		final int achieveRate;
	}
}
