package com.newbarams.ajaja.module.plan.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.domain.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;

@RestController
@RequestMapping("/plans")
public class PlanController {
	private final CreatePlanService createPlanService;

	public PlanController(CreatePlanService createPlanService) {
		this.createPlanService = createPlanService;
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public AjajaResponse<PlanResponse.GetOne> createPlan(@RequestBody PlanRequest.Create request) {
		PlanResponse.GetOne response = createPlanService.create(request);

		return new AjajaResponse<>(true, response);
	}
}
