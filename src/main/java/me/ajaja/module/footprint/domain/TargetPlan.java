package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class TargetPlan extends SelfValidating<TargetPlan> {
	@NotNull
	private final Long targetPlanId;

	@NotBlank
	@Size(max = 20)
	private final String title;

	@ConstructorProperties({"targetPlanId", "title"})
	public TargetPlan(Long targetPlanId, String title) {
		this.targetPlanId = targetPlanId;
		this.title = title;
		this.validateSelf();
	}
}
