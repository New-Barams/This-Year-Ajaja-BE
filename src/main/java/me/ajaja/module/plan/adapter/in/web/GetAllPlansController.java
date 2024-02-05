package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.module.plan.application.port.in.LoadAllPlansUseCase;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

@RestController
@RequiredArgsConstructor
public class GetAllPlansController {
	private final LoadAllPlansUseCase loadAllPlansUseCase;

	@GetMapping("/plans")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = loadAllPlansUseCase.loadAllPlans(request);
		return AjajaResponse.ok(responses);
	}
}
