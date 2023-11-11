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
	private boolean canRemind;
	private boolean canAjaja;
	private boolean isDeleted;

	public PlanStatus(boolean isPublic) {
		this.isPublic = isPublic;
		this.canRemind = true;
		this.canAjaja = true;
		this.isDeleted = false;
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

	void update(boolean isPublic, boolean canRemind, boolean canAjaja) {
		this.isPublic = isPublic;
		this.canRemind = canRemind;
		this.canAjaja = canAjaja;
	}
}
