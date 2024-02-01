package me.ajaja.module.ajaja.application;

import static me.ajaja.module.ajaja.domain.Ajaja.Type.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaRepository;
import me.ajaja.module.plan.application.LoadPlanService;
import me.ajaja.module.plan.domain.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchAjajaService {
	private final AjajaRepository ajajaRepository;
	private final LoadPlanService loadPlanService;

	public void switchOrAddIfNotExist(Long userId, Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);

		Ajaja ajaja = ajajaRepository.findByTargetIdAndUserIdAndType(planId, userId, PLAN.name())
			.orElseGet(Ajaja::defaultValue);

		if (ajaja.isEqualsDefault()) {
			addToPlan(plan, userId);
			return;
		}

		switchStatus(ajaja);
	}

	private void addToPlan(Plan plan, Long userId) {
		Ajaja ajaja = Ajaja.plan(plan.getId(), userId);
		ajajaRepository.save(ajaja);
	}

	private void switchStatus(Ajaja ajaja) {
		ajaja.switchStatus();
		ajajaRepository.save(ajaja);
	}
}
