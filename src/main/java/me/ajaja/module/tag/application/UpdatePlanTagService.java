package me.ajaja.module.tag.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanTagService {
	private final CreatePlanTagsService createPlanTagService;
	private final DeletePlanTagService deletePlanTagService;

	public List<String> update(Long planId, List<String> tags) {
		deletePlanTagService.delete(planId);

		return createPlanTagService.create(planId, tags);
	}
}
