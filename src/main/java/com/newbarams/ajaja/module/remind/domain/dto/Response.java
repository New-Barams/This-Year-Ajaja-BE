package com.newbarams.ajaja.module.remind.domain.dto;

import java.sql.Timestamp;

public class Response {
	public String remindMessage;
	public boolean feedBack;
	public int rate;
	public Timestamp deadLine;

	public Response(String remindMessage, boolean feedBack, int rate, Timestamp deadLine) {
		this.remindMessage = remindMessage;
		this.feedBack = feedBack;
		this.rate = rate;
		this.deadLine = deadLine;
	}
}
