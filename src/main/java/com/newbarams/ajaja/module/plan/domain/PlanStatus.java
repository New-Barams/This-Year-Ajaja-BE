package com.newbarams.ajaja.module.plan.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanStatus {
	private boolean isPublic;
	private boolean isRemindable;
	private boolean isDeleted;

	public PlanStatus(boolean isPublic) {
		this.isPublic = isPublic;
		this.isRemindable = true;
		this.isDeleted = false;
	}

	void changeToDeleted() {
		this.isDeleted = true;
	}

	void changePublicOrNot() {
		this.isPublic = !isPublic;
	}

	void changeRemindableOrNot() {
		this.isRemindable = !isRemindable;
	}
}
