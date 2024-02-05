package me.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.application.port.in.LoadAllPlansUseCase;
import me.ajaja.module.plan.application.port.out.FindAllPlansPort;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class LoadAllPlansService implements LoadAllPlansUseCase {
	private final FindAllPlansPort findAllPlansPort;

	@Override
	public List<PlanResponse.GetAll> loadAllPlans(PlanRequest.GetAll request) {
		return findAllPlansPort.findAllByCursorAndSorting(request);
	}
}
