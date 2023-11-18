package com.newbarams.ajaja.module.ajaja.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.ajaja.domain.AjajaRepository;
import com.newbarams.ajaja.module.plan.application.GetPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchAjajaService {
	private final AjajaRepository ajajaRepository;
	private final GetPlanService getPlanService;

	public void switchOrAddIfNotExist(Long userId, Long planId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(planId);

		Ajaja ajaja = plan.getAjajaByUserId(userId);

		if (ajaja == null) {
			addToPlan(plan, userId);
			return;
		}

		ajaja.switchStatus();
	}

	private void addToPlan(Plan plan, Long userId) {
		Ajaja ajaja = Ajaja.plan(plan.getId(), userId);
		plan.addAjaja(ajaja);
		ajajaRepository.save(ajaja);
	}
}
