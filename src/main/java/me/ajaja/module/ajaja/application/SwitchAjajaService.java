package me.ajaja.module.ajaja.application;

import static me.ajaja.global.exception.ErrorCode.*;
import static me.ajaja.module.ajaja.domain.Ajaja.Type.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaRepository;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.domain.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchAjajaService {
	private final AjajaRepository ajajaRepository;
	private final FindPlanPort findPlanPort;

	public void switchOrAddIfNotExist(Long userId, Long planId) {
		Plan plan = findPlanPort.findById(planId)
			.orElseThrow(() -> AjajaException.withId(planId, NOT_FOUND_PLAN));

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
