package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class TargetPlan extends SelfValidating<TargetPlan> {
	@NotNull
	private final Long targetPlanId;

	@ConstructorProperties("targetPlanId")
	public TargetPlan(Long targetPlanId) {
		this.targetPlanId = targetPlanId;
		this.validateSelf();
	}
}
