package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindInfo extends SelfValidating<RemindInfo> {
	public enum RemindTime {
		MORNING,
		AFTERNOON,
		EVENING
	}

	@Positive
	private int remindTotalPeriod;

	@Positive
	private int remindTerm;

	@Positive
	private int remindDate;

	@Enumerated(value = EnumType.STRING)
	private RemindTime remindTime;

	public RemindInfo(int remindTotalPeriod, int remindTerm, int remindDate, String remindTime) {
		this.remindTotalPeriod = remindTotalPeriod;
		this.remindTerm = remindTerm;
		this.remindDate = remindDate;
		this.remindTime = RemindTime.valueOf(remindTime);
		this.validateSelf();
	}

	void update(int remindTotalPeriod, int remindTerm, int remindDate, String remindTime) {
		this.remindTotalPeriod = remindTotalPeriod;
		this.remindTerm = remindTerm;
		this.remindDate = remindDate;
		this.remindTime = RemindTime.valueOf(remindTime);
		this.validateSelf();
	}

	public int getRemindMonth() {
		return this.remindTerm == 1 ? 2 : this.remindTerm;
	}

	public int getTotalRemindUmber() {
		int totalRemindUmber = this.remindTotalPeriod / this.remindTerm;

		return this.remindTerm == 1 ? totalRemindUmber - 1 : totalRemindUmber;
	}

	public int getRemindTime(String name) {
		if (name.equals("MORNING")) {
			return 9;
		} else if (name.equals("AFTERNOON")) {
			return 13;
		} else {
			return 22;
		}
	}
}
