package com.newbarams.ajaja.module.plan.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanStatus {
	private boolean isPublic;
	private boolean canRemind;
	private boolean canAjaja;
	private boolean isDeleted;

	public PlanStatus(boolean isPublic) {
		this(isPublic, true, true, false);
	}

	void toDeleted() {
		this.isDeleted = true;
	}

	void switchPublic() {
		this.isPublic = !isPublic;
	}

	void switchRemind() {
		this.canRemind = !canRemind;
	}

	void switchAjaja() {
		this.canAjaja = !canAjaja;
	}

	PlanStatus update(boolean isPublic, boolean canRemind, boolean canAjaja) {
		return new PlanStatus(isPublic, canRemind, canAjaja, isDeleted);
	}

	PlanStatus disable() {
		return new PlanStatus(isPublic, false, false, true);
	}
}
