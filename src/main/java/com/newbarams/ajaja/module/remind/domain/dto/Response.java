package com.newbarams.ajaja.module.remind.domain.dto;

import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	Long userId;
	Long planId;
	String email;
	int idx;
	int remindTerm;
	RemindInfo info;

	@QueryProjection
	public Response(Long userId, Long planId, String email, int idx, int remindTerm, RemindInfo info) {
		this.userId = userId;
		this.planId = planId;
		this.email = email;
		this.idx = idx;
		this.remindTerm = remindTerm;
		this.info = info;
	}
}



