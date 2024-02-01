package me.ajaja.module.plan.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlanStatus {
	private boolean isPublic;
	private boolean canRemind;
	private boolean canAjaja;
	private boolean deleted;

	public PlanStatus(boolean isPublic, boolean canAjaja) {
		this(isPublic, true, canAjaja, false);
	}

	void toDeleted() {
		this.deleted = true;
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

	PlanStatus disable() {
		return new PlanStatus(isPublic, false, false, true);
	}

	PlanStatus update(boolean isPublic, boolean canAjaja) {
		return new PlanStatus(isPublic, canRemind, canAjaja, deleted);
	}
}
