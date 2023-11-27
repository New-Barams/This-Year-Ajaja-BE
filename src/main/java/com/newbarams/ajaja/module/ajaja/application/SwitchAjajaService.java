package com.newbarams.ajaja.module.ajaja.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.ajaja.domain.repository.AjajaRepository;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchAjajaService {
	private final AjajaRepository ajajaRepository;
	private final LoadPlanService loadPlanService;

	public void switchOrAddIfNotExist(Long userId, Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);

		Ajaja ajaja = plan.getAjajaByUserId(userId);

		if (ajaja.isEqualsDefault()) {
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
