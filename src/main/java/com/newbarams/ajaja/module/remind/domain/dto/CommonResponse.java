package com.newbarams.ajaja.module.remind.domain.dto;

import java.util.List;

public class CommonResponse {
	public String remindTime;
	public int remindDate;
	public int remindTerm;
	public int remindTotalPeriod;
	public boolean isRemindable;
	public List<Response> responses;

	public CommonResponse(String remindTime, int remindDate, int remindTerm, int remindTotalPeriod,
		boolean isRemindable,
		List<Response> responses) {
		this.remindTime = remindTime;
		this.remindDate = remindDate;
		this.remindTerm = remindTerm;
		this.remindTotalPeriod = remindTotalPeriod;
		this.isRemindable = isRemindable;
		this.responses = responses;
	}
}
