package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.module.plan.application.port.out.FindAllPlansQuery;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

@RestController
@RequiredArgsConstructor
class GetAllPlansController {
	private final FindAllPlansQuery findAllPlansQuery;

	@GetMapping("/plans")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = findAllPlansQuery.findAllByCursorAndSorting(request);
		return AjajaResponse.ok(responses);
	}
}
