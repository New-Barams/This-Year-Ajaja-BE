package me.ajaja.module.plan.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.SelfValidating;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindInfo extends SelfValidating<RemindInfo> {
	@Getter
	@RequiredArgsConstructor
	private enum RemindTime {
		MORNING(9),
		AFTERNOON(13),
		EVENING(22);

		private final int time;
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
		this.remindTime = RemindTime.valueOf(remindTime.toUpperCase());
		this.validateSelf();
	}

	public int getRemindTime() {
		return remindTime.getTime();
	}

	public String getRemindTimeName() {
		return remindTime.name();
	}

	public int getRemindMonth() {
		return this.remindTerm == 1 ? 2 : this.remindTerm;
	}

	public int getTotalRemindNumber() {
		int totalRemindNumber = this.remindTotalPeriod / this.remindTerm;

		return this.remindTerm == 1 ? totalRemindNumber - 1 : totalRemindNumber;
	}
}
