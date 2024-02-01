package me.ajaja.module.tag.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.domain.repository.PlanTagRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlanTagService {
	private final PlanTagRepository planTagRepository;

	public void delete(Long planId) {
		planTagRepository.deleteAllByPlanId(planId);
	}
}
