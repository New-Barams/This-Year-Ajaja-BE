package com.newbarams.ajaja.module.tag.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.tag.domain.repository.PlanTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlanTagService {
	private final PlanTagRepository planTagRepository;

	public void delete(Long planId) {
		planTagRepository.deleteAllByPlanId(planId);
	}
}
